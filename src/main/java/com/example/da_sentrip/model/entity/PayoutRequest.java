package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "PAYOUT_REQUESTS")
@Getter
@Setter
public class PayoutRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ORDER_CODE")
    private String orderCode;

    @ManyToOne
    @JoinColumn(name = "MERCHANT_ID")
    private Merchant merchant;

    @Column(name = "ORDER_AMOUNT")
    private BigDecimal orderAmount;

    @Column(name = "PAYOUT_AMOUNT")
    private BigDecimal payoutAmount;

    @Column(name = "STATUS")
    private String status = "PENDING";

    @Column(name = "BILL_FILE_NAME")
    private String billFileName;

    @Column(name = "BILL_FILE_URL", length = 1000)
    private String billFileUrl;

    @Column(name = "REQUESTED_AT")
    private LocalDateTime requestedAt;

    @Column(name = "PROCESSED_AT")
    private LocalDateTime processedAt;

    @Column(name = "PROCESSED_BY")
    private Long processedBy;
}