package com.example.da_sentrip.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentRequestDTO {
    private String orderCode;
    private String bankName;
    private String bankAccount;
    private Long employeeId;
}