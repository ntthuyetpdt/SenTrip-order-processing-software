package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.OrderDTO;
import com.example.da_sentrip.model.dto.reponse.OrderReponseDTO;
import com.example.da_sentrip.model.dto.request.OrderRequestDTO;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface OrderService {

    OrderDTO Order(OrderRequestDTO request, String gmail);
    List<OrderReponseDTO> Getall();
    OrderDTO Cancel(String oderCode);
    List<OrderReponseDTO> getMyOrders(Authentication authentication);
}