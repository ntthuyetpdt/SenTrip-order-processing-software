package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.reponse.PaymentResponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.PaymentConfirmRequestDTO;
import com.example.da_sentrip.model.dto.request.PaymentRequestDTO;
import com.example.da_sentrip.service.PaymentService;
import com.example.da_sentrip.helper.QR;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/confirmation")
    public ResponseEntity<?> confirm(@RequestParam("url") String url) {
        try{
            PaymentResponseDTO result = paymentService.confirmPaymentByUrl(url);
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("payment confirmed")
                    .data(result)
                    .build());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.builder()
                            .status("payment confirmation failed")
                            .code(Constants.HTTP_STATUS.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .message(ex.getMessage())
                            .build());
        }
    }
}