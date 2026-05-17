package com.example.prs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.prs.model.enums.PaymentMethod;
import com.example.prs.model.enums.PaymentStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false, unique = true)
    private Order order;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentMethod method;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private LocalDateTime paidAt;
    
}
