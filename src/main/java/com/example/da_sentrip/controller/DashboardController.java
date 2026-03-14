package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.reponse.MonthlyRevenueDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.MerchantDashboardRequestDTO;
import com.example.da_sentrip.service.DashboardService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PostMapping("/summary")
    @PreAuthorize("hasAnyAuthority('ADMIN_VIEW_DASHBOARD')")
    public ResponseEntity<ResponseDTO> getSummary(@RequestBody(required = false) MerchantDashboardRequestDTO request) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get summary success")
                .data(dashboardService.getDashboardSummary(request))
                .build());
    }

    @PostMapping("/monthly-revenue")
    @PreAuthorize("hasAnyAuthority('ADMIN_SALES')")
    public ResponseEntity<ResponseDTO> getMonthlyRevenue(@RequestBody(required = false) MerchantDashboardRequestDTO request) {
        List<MonthlyRevenueDTO> data = dashboardService.getMonthlyRevenue(request);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get monthly revenue success")
                .data(data)
                .total((long) data.size())
                .build());
    }


}