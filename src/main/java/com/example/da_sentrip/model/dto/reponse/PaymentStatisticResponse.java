package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PaymentStatisticResponse {
    private Long totalTransactions;
    private BigDecimal totalRevenue;
}
