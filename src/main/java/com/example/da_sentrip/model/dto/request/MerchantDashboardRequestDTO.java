package com.example.da_sentrip.model.dto.request;

import com.google.firebase.database.annotations.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MerchantDashboardRequestDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}