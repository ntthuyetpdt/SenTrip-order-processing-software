package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.reponse.DashboardSummaryDTO;
import com.example.da_sentrip.model.dto.reponse.MonthlyRevenueDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.MerchantDashboardRequestDTO;
import com.example.da_sentrip.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    @PostMapping("/summary")
    public  ResponseEntity<DashboardSummaryDTO> getSummary(@RequestBody MerchantDashboardRequestDTO request) {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(request);
        return ResponseEntity.ok(summary);
    }
    @PostMapping("/monthly-revenue")
    public ResponseEntity<ResponseDTO<List<MonthlyRevenueDTO>>> getMonthlyRevenue(@RequestBody MerchantDashboardRequestDTO request) {

        List<MonthlyRevenueDTO> data = dashboardService.getMonthlyRevenue(request);
        long total = data.size();
        ResponseDTO<List<MonthlyRevenueDTO>> response = ResponseDTO.<List<MonthlyRevenueDTO>>builder()
                .status("SUCCESS")
                .code(200)
                .message("Lay doanh thu theo thang thanh cong")
                .data(data)
                .total(total)
                .details(String.valueOf(total))
                .build();
        return ResponseEntity.ok(response);
    }
}