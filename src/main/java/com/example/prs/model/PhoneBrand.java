package com.example.prs.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PhoneBrands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;
}
