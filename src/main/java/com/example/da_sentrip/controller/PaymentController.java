package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.request.PaymentRequestDTO;
import com.example.da_sentrip.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<?> payOrder(@RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.payOrder(request));
    }
}