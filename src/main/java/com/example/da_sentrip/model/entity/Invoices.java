package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter

@Entity
@Table(name = "invoices")

public class Invoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_code", unique = true, nullable = false)
    private String invoiceCode;

    @Column(name = "ORDER_ID")
    private Long oderId;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "pdf_part")
    private String pdfPart;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url", length = 1000)
    private String fileUrl;

    @Column(name = "GENRATED_AT")
    private LocalDateTime generatedAt;
}
