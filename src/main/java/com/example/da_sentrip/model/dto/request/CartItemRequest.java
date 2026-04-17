package com.example.da_sentrip.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CartItemRequest {
    private LocalDate NSD;
    private Integer quantity;
}
