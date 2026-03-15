package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PayoutRequestResponse {
    private Long id;
    private String orderCode;
    private Long merchantId;
    private String merchantName;
    private String bankName;
    private String bankAccount;
    private BigDecimal orderAmount;
    private BigDecimal payoutAmount;
    private String status;
    private String billFileUrl;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
}