package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.model.dto.reponse.PaymentResponseDTO;
import com.example.da_sentrip.model.dto.request.PaymentRequestDTO;
import com.example.da_sentrip.repository.OrderRepository;
import com.example.da_sentrip.repository.PaymentRepository;
import com.example.da_sentrip.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServicelmpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Value("${app.public-base-url}")
    private String publicBaseUrl;

    @Override
    @Transactional
    public PaymentResponseDTO payOrder(PaymentRequestDTO request) {
        OrderRepository.OrderPaySnapshot order = orderRepository.lockByOrderCode(request.getOrderCode()).orElseThrow(() -> new RuntimeException("ORDER NOT FOUND"));
        if (!"PENDING".equalsIgnoreCase(order.getOrderStatus())
                && !"PENDING_PAYMENT".equalsIgnoreCase(order.getOrderStatus())) {
            throw new RuntimeException("ORDER IS NOT PAYABLE");
        }
        BigDecimal amount = order.getTotalAmount();
        String paymentCode = "PM-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
        paymentRepository.insertWaiting(paymentCode, order.getId(), amount);
        Optional<Long> paymentId = paymentRepository.findIdByPaymentCode(paymentCode);
        if (paymentId.isEmpty()) {
            throw new RuntimeException("PAYMENT INSERT FAILED");
        }
        paymentRepository.insertLog(
                paymentId.orElse(null),
                "CREATE_PAYMENT",
                "PENDING",
                "PENDING",
                amount,
                amount,
                null
        );
        String paymentUrl = publicBaseUrl + "/payments/" + paymentCode;
        String qrUrl = publicBaseUrl + "/payments/" + paymentCode + "/qr";
        return new PaymentResponseDTO(
                paymentCode,
                request.getOrderCode(),
                amount,
                "WAITING_FOR_PAYMENT",
                (LocalDateTime) null,
                paymentUrl,
                qrUrl
        );
    }

    @Override
    @Transactional
    public PaymentResponseDTO confirmPaymentByUrl(String url) {

        String paymentCode = extractPaymentCode(url);
        PaymentRepository.PaymentSnapshot pay = paymentRepository.lockByPaymentCode(paymentCode).orElseThrow(() -> new RuntimeException("PAYMENT NOT FOUND"));
        if ("SUCCESS".equalsIgnoreCase(pay.getPaymentStatus())) {
            String paymentUrl = publicBaseUrl + "/payments/" + pay.getPaymentCode();
            String qrUrl = paymentUrl + "/qr";
            return new PaymentResponseDTO(
                    pay.getPaymentCode(),
                    null,
                    pay.getAmount(),
                    "SUCCESS",
                    LocalDateTime.now(),
                    paymentUrl,
                    qrUrl
            );
        }
        if (!"WAITING_FOR_PAYMENT".equalsIgnoreCase(pay.getPaymentStatus())
                && !"PENDING".equalsIgnoreCase(pay.getPaymentStatus())) {
            throw new RuntimeException("PAYMENT IS NOT CONFIRMABLE");
        }
        paymentRepository.insertLog(
                pay.getId(),
                "UPDATE_STATUS",
                pay.getPaymentStatus(),
                "SUCCESS",
                pay.getAmount(),
                pay.getAmount(),
                null
        );

        paymentRepository.markSuccess(pay.getId());
        String paymentUrl = publicBaseUrl + "/payments/" + paymentCode;
        String qrUrl = paymentUrl + "/qr";

        return new PaymentResponseDTO(
                paymentCode,
                null,
                pay.getAmount(),
                "SUCCESS",
                LocalDateTime.now(),
                paymentUrl,
                qrUrl
        );
    }
    private String extractPaymentCode(String url) {
        if (url == null || url.isBlank()) throw new RuntimeException("URL IS REQUIRED");
        int idx = url.indexOf("/payments/");
        if (idx < 0) throw new RuntimeException("INVALID PAYMENT URL");
        String codePart = url.substring(idx + "/payments/".length());
        if (codePart.endsWith("/")) codePart = codePart.substring(0, codePart.length() - 1);
        int slash = codePart.indexOf("/");
        if (slash > 0) codePart = codePart.substring(0, slash);
        if (codePart.isBlank()) throw new RuntimeException("INVALID PAYMENT CODE");
        return codePart;
    }
}