package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.reponse.DashboardSummaryDTO;
import com.example.da_sentrip.model.dto.reponse.MonthlyRevenueDTO;
import com.example.da_sentrip.model.dto.request.MerchantDashboardRequestDTO;
import java.util.List;

public interface DashboardService {
    DashboardSummaryDTO getDashboardSummary(MerchantDashboardRequestDTO request);
    List<MonthlyRevenueDTO> getMonthlyRevenue(MerchantDashboardRequestDTO request);
}
