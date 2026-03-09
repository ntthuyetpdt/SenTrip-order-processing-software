package com.example.da_sentrip.model.dto.reponse.view;
import java.math.BigDecimal;

public interface MerchantDashboardView {
    BigDecimal getTotalRevenue();
    Long getTotalCustomers();
    Long getTotalOrders();
}