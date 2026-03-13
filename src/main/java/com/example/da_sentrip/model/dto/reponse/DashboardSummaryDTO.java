package com.example.da_sentrip.model.dto.reponse;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class DashboardSummaryDTO {
    private Long getTotalOrders;
    private Long getCancelledOrders;
    private Long getSuccessOrders;
    private Long getTotalCustomers;
    private Long getTotalMerchants;
    private Long getTotalEmployees;
}
