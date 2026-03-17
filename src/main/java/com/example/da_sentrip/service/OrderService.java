package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.OrderDTO;
import com.example.da_sentrip.model.dto.reponse.OderdetailReponseDTO;
import com.example.da_sentrip.model.dto.reponse.OrderAllReponseDTO;
import com.example.da_sentrip.model.dto.reponse.OrderDetailResponse;
import com.example.da_sentrip.model.dto.reponse.OrderReponseDTO;
import com.example.da_sentrip.model.dto.reponse.view.GetallOder;
import com.example.da_sentrip.model.dto.reponse.view.OrderDetailDTO;
import com.example.da_sentrip.model.dto.request.OrderRequestDTO;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderDTO Order(OrderRequestDTO request, String gmail);
    List<OrderAllReponseDTO> Getall(Authentication authentication);
    List<GetallOder> searchOrder(String orderCode, String address, BigDecimal minPrice, BigDecimal maxPrice, String sortByPrice,String orderStatus);
    OrderDTO Cancel(String oderCode);
    List<OrderReponseDTO> getMyOrders(Authentication authentication);
    List<OderdetailReponseDTO> details (String  orderCode);
    OrderDetailResponse getOrderDetail(String orderCode);
    void updateOrderStatus(String orderCode, String orderStatus);
    List<OrderDetailDTO> searchByMerchant(Authentication authentication, String orderCode, String productName, BigDecimal minPrice, BigDecimal maxPrice, LocalDateTime startDate, LocalDateTime endDate);
}