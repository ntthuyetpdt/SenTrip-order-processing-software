package com.example.da_sentrip.model.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDTO {
    private Long userId;
    private Long merchantId;
    private List<Item> items;

    @Data
    public static class Item {
        private Long productId;
        private Integer quantity;
    }
}