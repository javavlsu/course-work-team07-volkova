package com.example.prs.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "RepairServices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepairService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedPrice;

    private Long estimatedDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phoneModelId", nullable = false)
    private PhoneModel phoneModel;
}
