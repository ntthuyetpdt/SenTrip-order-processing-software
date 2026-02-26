package com.example.da_sentrip.model.dto;

import com.example.da_sentrip.model.dto.reponse.UserOrderDTO;
import com.example.da_sentrip.model.entity.Merchant;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.model.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class OrderDTO {

    private String orderCode;
    private UserOrderDTO user;
    private Merchant merchant;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
