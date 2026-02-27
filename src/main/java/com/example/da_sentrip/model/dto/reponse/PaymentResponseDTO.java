package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResponseDTO {
    private String paymentCode;
    private String orderCode;
    private BigDecimal amount;
    private String paymentStatus;
    private String bankName;
    private LocalDateTime paidAt;
}

