package com.zerfinit.zerfinit_Api.auth.service;

import com.zerfinit.zerfinit_Api.auth.model.AuthResponse;
import com.zerfinit.zerfinit_Api.auth.model.User;
import com.zerfinit.zerfinit_Api.auth.model.VerificationToken;
import com.zerfinit.zerfinit_Api.auth.repository.UserRepository;
import com.zerfinit.zerfinit_Api.auth.repository.VerificationTokenRepository;
import com.zerfinit.zerfinit_Api.config.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${spring.mail.username}")
    private String fromEmail; // Inject the sender email from properties

    public AuthService(UserRepository userRepository,
                       VerificationTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       JavaMailSender mailSender,
                       AuthenticationManager authenticationManager,
                       JwtTokenUtil jwtTokenUtil,
                       UserDetailsServiceImpl userDetailsService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    public User register(User user) throws MessagingException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("STUDENT"); // Default role
        User savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);

        sendVerificationEmail(savedUser.getEmail(), token);

        return savedUser;
    }

    private void sendVerificationEmail(String toEmail, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(fromEmail); // Set the "from" address explicitly
        helper.setTo(toEmail);     // Set the "to" address to the customer's email
        helper.setSubject("Verify Your Email");
        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token;
        helper.setText("Please click the link to verify your email: <a href='" + verificationUrl + "'>Verify Email</a>", true);

        System.out.println("Sending verification email from: " + fromEmail + " to: " + toEmail); // Debug
        mailSender.send(message);
        System.out.println("Verification email sent successfully to: " + toEmail); // Debug
    }

    public String verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);

        return "Email verified successfully";
    }

    public AuthResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Please verify your email first");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        String token = jwtTokenUtil.generateToken(userDetailsService.loadUserByUsername(email));
        return new AuthResponse(token, user.getEmail(), user.getFirstname(), user.getLastname(), user.getRole(), user.getUsername());
    }
}