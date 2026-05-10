package com.example.prs.model;

import com.example.prs.model.enums.UserRole;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String login;

    @Column(nullable = false, length = 255)
    private String password;
    
    @Column(nullable = false, length = 100)
    private String firstName;
    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(unique = true, length = 255)
    private String email;

    @Column(unique = true, nullable = false, length = 20)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    public User(Long id, String login, String password, String firstName, String lastName, String email, String number, UserRole role) {
    this.id = id;
    this.login = login;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.number = number;
    this.role = role;
}
}
