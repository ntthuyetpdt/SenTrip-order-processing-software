package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceResponse {
    private String orderCode;
    private String invoiceCode;
    private BigDecimal amount;
    private String status;
    private String fileName;
    private LocalDateTime generatedAt;
    private String fileUrl;
}