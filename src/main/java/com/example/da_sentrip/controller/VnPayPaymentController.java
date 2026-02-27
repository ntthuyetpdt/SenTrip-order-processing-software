//package com.example.da_sentrip.controller;
//
//import com.example.da_sentrip.model.dto.reponse.CreateVnPayPaymentResponse;
//import com.example.da_sentrip.model.dto.reponse.PaymentResponseDTO;
//import com.example.da_sentrip.service.VnPayPaymentService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/v1/payments/vnpay")
//public class VnPayPaymentController {
//
//    private final VnPayPaymentService vnPayPaymentService;
//
//    @PostMapping("/create-url")
//    public ResponseEntity<CreateVnPayPaymentResponse> createUrl(
//            @RequestParam String orderCode,
//            HttpServletRequest request
//    ) {
//        String ip = request.getRemoteAddr();
//        return ResponseEntity.ok(vnPayPaymentService.createPaymentUrl(orderCode, ip));
//    }
//
//    @GetMapping("/return")
//    public ResponseEntity<PaymentResponseDTO> handleReturn(@RequestParam Map<String, String> params) {
//        return ResponseEntity.ok(vnPayPaymentService.handleReturn(params));
//    }
//
//    @GetMapping("/ipn")
//    public ResponseEntity<String> handleIpn(@RequestParam Map<String, String> params) {
//        return ResponseEntity.ok(vnPayPaymentService.handleIpn(params));
//    }
//}