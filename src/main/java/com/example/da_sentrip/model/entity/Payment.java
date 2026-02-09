package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "PAYMENTS")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PAYMENT_CODE")
    private String paymentCode;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;

    @ManyToOne
    @JoinColumn(name = "PROCESSED_BY")
    private Employee processedBy;

    @Column(name = "PAID_AT")
    private LocalDateTime paidAt;
}
