package com.example.prs.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PhoneModels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phoneBrandId", nullable = false)
    private PhoneBrand phoneBrand;
}
