package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.PaymentRequestDTO;
import com.example.da_sentrip.service.PaymentService;
import com.example.da_sentrip.helper.CreateQR;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final CreateQR qrCodeService;

    @Value("${app.public-base-url}")
    private String publicBaseUrl;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER_PAY')")
    public ResponseEntity<ResponseDTO> payOrder(@RequestBody PaymentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.CREATED)
                .message("Create payment successfully")
                .data(paymentService.payOrder(request))
                .build());
    }

    @GetMapping(value = "/{paymentCode}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasAnyAuthority('CUSTOMER_GET_URLPAY')")
    public ResponseEntity<byte[]> getPaymentQr(@PathVariable String paymentCode) {
        byte[] qrImage = qrCodeService.generateQr(publicBaseUrl + "/payments/" + paymentCode, 300, 300);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrImage);
    }

    @PostMapping("/confirmation")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_PAY_CONFIRM')")
    public ResponseEntity<ResponseDTO> confirm(@RequestParam String url) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Payment confirmed")
                .data(paymentService.confirmPaymentByUrl(url))
                .build());
    }
}