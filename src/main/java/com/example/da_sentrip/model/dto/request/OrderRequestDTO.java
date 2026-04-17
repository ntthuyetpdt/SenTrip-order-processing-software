package com.example.da_sentrip.model.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequestDTO {
    private Long userId;
    private Long merchantId;
    private List<Item> items;
    private LocalDate NSD;

    @Data
    public static class Item {
        private Long productId;
        private Integer quantity;

    }
}