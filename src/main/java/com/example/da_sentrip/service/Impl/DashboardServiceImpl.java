package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.model.dto.reponse.DashboardSummaryDTO;
import com.example.da_sentrip.model.dto.reponse.MonthlyRevenueDTO;
import com.example.da_sentrip.model.dto.reponse.view.DashboardSummaryProjection;
import com.example.da_sentrip.model.dto.reponse.view.MonthlyRevenueProjection;
import com.example.da_sentrip.model.dto.request.MerchantDashboardRequestDTO;
import com.example.da_sentrip.repository.MerchantRepository;
import com.example.da_sentrip.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class DashboardServiceImpl implements DashboardService {

    private final MerchantRepository merchantRepository;

    @Override
    public DashboardSummaryDTO getDashboardSummary(MerchantDashboardRequestDTO request) {
        DashboardSummaryProjection projection = merchantRepository.getDashboardSummary(
                request.getStartDate(),
                request.getEndDate()
        );

        if (projection == null) {
            return new DashboardSummaryDTO(0L, 0L, 0L, 0L, 0L, 0L);
        }

        return new DashboardSummaryDTO(
                projection.getTotalOrders() != null ? projection.getTotalOrders() : 0L,
                projection.getCancelledOrders() != null ? projection.getCancelledOrders() : 0L,
                projection.getSuccessOrders() != null ? projection.getSuccessOrders() : 0L,
                projection.getTotalCustomers() != null ? projection.getTotalCustomers() : 0L,
                projection.getTotalMerchants() != null ? projection.getTotalMerchants() : 0L,
                projection.getTotalEmployees() != null ? projection.getTotalEmployees() : 0L
        );
    }

    @Override
    public List<MonthlyRevenueDTO> getMonthlyRevenue(MerchantDashboardRequestDTO request) {
        List<MonthlyRevenueProjection> projections = merchantRepository.getTotalRevenue(request.getStartDate(), request.getEndDate());

        if (projections == null || projections.isEmpty()) {
            return Collections.emptyList();
        }
        return projections.stream()
                .map(item -> new MonthlyRevenueDTO(
                        item.getMonth(),
                        item.getRevenue() != null ? item.getRevenue() : BigDecimal.ZERO
                ))
                .collect(Collectors.toList());
    }
    }
