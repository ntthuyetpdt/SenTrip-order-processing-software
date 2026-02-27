package com.example.da_sentrip.model.dto.reponse;

import com.example.da_sentrip.model.entity.Order;
import com.example.da_sentrip.model.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderReponseDTO {

    private String orderCode;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private UserOrderDTO user;
    private String productNames;

    public OrderReponseDTO(Order order) {
        this.orderCode = order.getOrderCode();
        this.orderStatus = order.getOrderStatus();
        this.totalAmount = order.getTotalAmount();
        this.createdAt = order.getCreatedAt();
        this.user = new UserOrderDTO(order.getUser());
    }

    public OrderReponseDTO(String orderCode, OrderStatus orderStatus, BigDecimal totalAmount, LocalDateTime createdAt, String productNames) {
        this.orderCode = orderCode;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.productNames = productNames;
    }
}