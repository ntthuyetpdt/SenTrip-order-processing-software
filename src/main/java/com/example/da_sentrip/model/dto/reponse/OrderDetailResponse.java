package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private String orderCode;
    private String fullName;
    private String cccd;
    private String phone;
    private String address;
    private String gmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal totalAmount;
    private String orderStatus;
    private String paymentStatus;
    private String paymentCode;
    private BigDecimal paymentAmount;
    private LocalDateTime paidAt;
    private String productName;
    private String quantity;
}