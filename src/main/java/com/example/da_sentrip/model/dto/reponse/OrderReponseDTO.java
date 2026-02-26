package com.example.da_sentrip.model.dto.reponse;

import com.example.da_sentrip.model.entity.Merchant;
import com.example.da_sentrip.model.entity.Order;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.model.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderReponseDTO {

    private String orderCode;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private UserOrderDTO user;

    public OrderReponseDTO(Order order) {
        this.orderCode = order.getOrderCode();
        this.orderStatus = order.getOrderStatus();
        this.totalAmount = order.getTotalAmount();
        this.createdAt = order.getCreatedAt();
        this.user = new UserOrderDTO(order.getUser());
    }
}