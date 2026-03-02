package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.request.PaymentRequestDTO;
import com.example.da_sentrip.service.PaymentService;
import com.example.da_sentrip.helper.QR;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final QR qrCodeService;

    @Value("${app.public-base-url}")
    private String publicBaseUrl;

    @PostMapping("/pay")
    public ResponseEntity<?> payOrder(@RequestBody PaymentRequestDTO request) {
        return ResponseEntity.ok(paymentService.payOrder(request));
    }

    @GetMapping(value = "/{paymentCode}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getPaymentQr(@PathVariable String paymentCode) {
        String paymentUrl = publicBaseUrl + "/payments/" + paymentCode;
        return qrCodeService.generateQr(paymentUrl, 300, 300);
    }
}