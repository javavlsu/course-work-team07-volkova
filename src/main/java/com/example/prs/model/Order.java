package com.example.prs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.prs.model.enums.OrderStatus;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime repairedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="phoneBrandId")
    private PhoneBrand phoneBrand;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="phoneModelId")
    private PhoneModel phoneModel;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="repairServiceId")
    private RepairService repairService;

    @Column(length = 1000)
    private String description;

    @Column(precision=10, scale=2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", nullable = true)
    private User client;

    private String contactEmail;
    @Column(length = 20)
    private String contactNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private User employee;
    
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;
}
