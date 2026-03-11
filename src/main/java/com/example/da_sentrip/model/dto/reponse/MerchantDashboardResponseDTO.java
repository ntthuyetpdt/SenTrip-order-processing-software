package com.example.da_sentrip.model.dto.reponse;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDashboardResponseDTO {
    private BigDecimal totalRevenue;
    private Long totalCustomers;
    private Long totalOrders;
    private Long successOrders;
    private Long cancelledOrders;
}