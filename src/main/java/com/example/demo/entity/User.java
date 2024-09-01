package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The email of the user.
     */
    private String email;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The role of the user.
     */
    private String role;

    /**
         * The authorities of the user.
         */
    private String authorities;

    /**
     * The enabled status of the user.
     */
    private boolean enabled;
}
