package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.OrderDTO;
import com.example.da_sentrip.model.dto.reponse.*;
import com.example.da_sentrip.model.dto.request.OrderRequestDTO;
import com.example.da_sentrip.service.OrderService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/getall")
    public ResponseEntity<SuccessResponse<List<OrderAllReponseDTO>>> getAll(Authentication authentication) {
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Get all success", orderService.Getall(authentication)));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_GETALL_ODER')")
    public ResponseEntity<SuccessResponse<List<OrderReponseDTO>>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Get my orders success", orderService.getMyOrders(authentication)));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_ODER_PRODUCT')")
    public ResponseEntity<ResponseDTO> create(@RequestBody OrderRequestDTO request, Authentication authentication) {
        OrderDTO order = orderService.Order(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.CREATED)
                .message("Order created success")
                .data(order)
                .build());
    }

    @GetMapping("detail/{orderCode}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_GETALL_ODER_DETAILS')")
    public ResponseEntity<ResponseDTO> detail(@PathVariable String orderCode) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get detail success")
                .data(orderService.details(orderCode))
                .build());
    }

    @PostMapping("delete/{orderCode}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_CANCLE_PRODUCT')")
    public ResponseEntity<ResponseDTO> cancel(@PathVariable String orderCode) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Cancel order success")
                .data(orderService.Cancel(orderCode))
                .build());
    }

}