package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.model.dto.reponse.PaymentResponseDTO;
import com.example.da_sentrip.model.dto.reponse.PaymentStatisticResponse;
import com.example.da_sentrip.model.dto.reponse.view.PaymentStatisticDTO;
import com.example.da_sentrip.model.dto.request.PaymentRequestDTO;
import com.example.da_sentrip.repository.OrderRepository;
import com.example.da_sentrip.repository.PaymentRepository;
import com.example.da_sentrip.repository.ProductRepository;
import com.example.da_sentrip.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaymentServicelmpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    @Value("${app.public-base-url}")
    private String publicBaseUrl;

    @Override
    @Transactional
    public PaymentResponseDTO payOrder(PaymentRequestDTO request) {
        OrderRepository.OrderPaySnapshot order = orderRepository.lockByOrderCode(request.getOrderCode())
                .orElseThrow(() -> new RuntimeException("ORDER NOT FOUND"));

        if (!"PENDING".equalsIgnoreCase(order.getOrderStatus())
                && !"PENDING_PAYMENT".equalsIgnoreCase(order.getOrderStatus()))
            throw new RuntimeException("ORDER IS NOT PAYABLE");

        BigDecimal amount = order.getTotalAmount();
        String paymentCode = "PM-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();

        paymentRepository.insertWaiting(paymentCode, order.getId(), amount);
        Long paymentId = paymentRepository.findIdByPaymentCode(paymentCode)
                .orElseThrow(() -> new RuntimeException("PAYMENT INSERT FAILED"));

        paymentRepository.insertLog(paymentId, "CREATE_PAYMENT", "PENDING", "PENDING", amount, amount, null);

        return buildResponse(paymentCode, request.getOrderCode(), amount, "WAITING_FOR_PAYMENT", null);
    }

    @Override
    @Transactional
    public PaymentResponseDTO confirmPaymentByUrl(String url) {
        String paymentCode = extractPaymentCode(url);
        PaymentRepository.PaymentSnapshot pay = paymentRepository.lockByPaymentCode(paymentCode)
                .orElseThrow(() -> new RuntimeException("PAYMENT NOT FOUND"));

        if ("SUCCESS".equalsIgnoreCase(pay.getPaymentStatus()))
            return buildResponse(pay.getPaymentCode(), null, pay.getAmount(), "SUCCESS", LocalDateTime.now());

        if (!"WAITING_FOR_PAYMENT".equalsIgnoreCase(pay.getPaymentStatus())
                && !"PENDING".equalsIgnoreCase(pay.getPaymentStatus()))
            throw new RuntimeException("PAYMENT IS NOT CONFIRMABLE");

        paymentRepository.insertLog(pay.getId(), "UPDATE_STATUS",
                pay.getPaymentStatus(), "SUCCESS", pay.getAmount(), pay.getAmount(), null);
        paymentRepository.markSuccess(pay.getId());

        return buildResponse(paymentCode, null, pay.getAmount(), "SUCCESS", LocalDateTime.now());
    }

    @Override
    public PaymentStatisticResponse getPaymentStatistic() {
        PaymentStatisticDTO dto = productRepository.findPaymentStatistic();
        return new PaymentStatisticResponse(
                dto.getTotalTransactions(),
                dto.getTotalRevenue()
        );
    }

    private PaymentResponseDTO buildResponse(String paymentCode, String orderCode,
                                             BigDecimal amount, String status, LocalDateTime paidAt) {
        String paymentUrl = publicBaseUrl + "/payments/" + paymentCode;
        return new PaymentResponseDTO(paymentCode, orderCode, amount, status, paidAt, paymentUrl, paymentUrl + "/qr");
    }

    private String extractPaymentCode(String url) {
        if (url == null || url.isBlank()) throw new RuntimeException("URL IS REQUIRED");
        int idx = url.indexOf("/payments/");
        if (idx < 0) throw new RuntimeException("INVALID PAYMENT URL");
        String code = url.substring(idx + "/payments/".length()).replaceAll("/$", "");
        int slash = code.indexOf("/");
        if (slash > 0) code = code.substring(0, slash);
        if (code.isBlank()) throw new RuntimeException("INVALID PAYMENT CODE");
        return code;
    }
}