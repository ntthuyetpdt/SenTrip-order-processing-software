package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.OrderDTO;
import com.example.da_sentrip.model.dto.request.OrderRequestDTO;
import com.example.da_sentrip.model.dto.reponse.OrderReponseDTO;
import com.example.da_sentrip.repository.OrderRepository;
import com.example.da_sentrip.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping("/getAll")
    public SuccessResponse<?> getAll(Authentication authentication) {
        List<OrderReponseDTO> orders = orderService.Getall();
        return new SuccessResponse<>(200, "Get the list of successful orders", orders);
    }

    @PostMapping("/create")
    public SuccessResponse<?> create(@RequestBody OrderRequestDTO request, Authentication authentication) {
        String gmail = authentication.getName();
        OrderDTO order = orderService.Order(request, gmail);
        return new SuccessResponse<>(200, "Order added successfully", order);
    }

    @GetMapping("/detail/{orderCode}")
    public SuccessResponse<?> detail(@PathVariable String orderCode) {
        List<Object[]> detail = orderRepository.findOrderDetailByOrderCode(orderCode);
        return new SuccessResponse<>(200, "GET ORDER DETAIL SUCCESS", detail);
    }

    @PostMapping("/cancel/{orderCode}")
    public SuccessResponse<?> cancel(@PathVariable String orderCode) {
        OrderDTO result = orderService.Cancel(orderCode);
        return new SuccessResponse<>(200, "Order cancelled successfully.", result);
    }
}