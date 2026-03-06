package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailResponseDTO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private String price;
    private BigDecimal quantity;
    private BigDecimal totalPrice;
    private String img;
}