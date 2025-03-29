package com.zerfinit.zerfinit_Api.auth.model;

import lombok.Data;


@Data
public class AuthResponse {
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String username;

    public AuthResponse(String token, String email, String firstName, String lastName, String role, String username) {
        this.token = token;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}