package com.example.da_sentrip.model.dto.reponse.view;

public interface DashboardSummaryProjection {
    Long getTotalOrders();
    Long getCancelledOrders();
    Long getSuccessOrders();
    Long getTotalCustomers();
    Long getTotalMerchants();
    Long getTotalEmployees();
}
