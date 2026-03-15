package com.example.da_sentrip.model.dto.reponse.view;

import java.math.BigDecimal;

public interface PaymentStatisticDTO {
    Long getTotalTransactions();
    BigDecimal getTotalRevenue();
}
