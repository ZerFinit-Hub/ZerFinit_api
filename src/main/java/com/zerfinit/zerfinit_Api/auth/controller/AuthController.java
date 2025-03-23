package com.zerfinit.zerfinit_Api.auth.controller;



import com.zerfinit.zerfinit_Api.auth.model.User;
import com.zerfinit.zerfinit_Api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    private final AuthService authService;

    @Autowired // ✅ This injects AuthService into the constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }
}