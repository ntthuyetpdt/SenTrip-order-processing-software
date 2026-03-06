package com.example.da_sentrip.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsDTO {
    private Long id;
    private BigDecimal cartId;
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal price;
}