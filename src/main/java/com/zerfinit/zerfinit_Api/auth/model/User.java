package com.zerfinit.zerfinit_Api.auth.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String role; // STUDENT, TUTOR, ADMIN


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}