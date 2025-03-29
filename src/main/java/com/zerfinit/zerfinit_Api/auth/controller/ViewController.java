package com.zerfinit.zerfinit_Api.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // Use @Controller for rendering views, not @RestController
public class ViewController {

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token); // Pass the token to the form
        return "reset-password"; // Refers to reset-password.html in templates
    }
}