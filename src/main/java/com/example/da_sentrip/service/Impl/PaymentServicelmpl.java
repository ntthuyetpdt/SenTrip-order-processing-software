package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.model.dto.reponse.PaymentResponseDTO;
import com.example.da_sentrip.model.dto.request.PaymentRequestDTO;
import com.example.da_sentrip.repository.OrderRepository;
import com.example.da_sentrip.repository.PaymentRepository;
import com.example.da_sentrip.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServicelmpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponseDTO payOrder(PaymentRequestDTO request) {

        OrderRepository.OrderPaySnapshot order = orderRepository.lockByOrderCode(request.getOrderCode()).orElseThrow(() -> new RuntimeException("ORDER NOT FOUND"));
        if (!"PENDING".equalsIgnoreCase(order.getOrderStatus()) && !"PENDING_PAYMENT".equalsIgnoreCase(order.getOrderStatus())) {
            throw new RuntimeException("ORDER IS NOT PAYABLE");
        }
        BigDecimal amount = order.getTotalAmount();
        String paymentCode = "PM-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        paymentRepository.insertPending(
                paymentCode,
                order.getId(),
                amount,
                request.getBankName(),
                request.getBankAccount()
        );

        Long paymentId = paymentRepository.findIdByPaymentCode(paymentCode);
        paymentRepository.insertLog(
                paymentId,
                "UPDATE_STATUS",
                "CONFIRM",
                "SUCCESS",
                amount,
                amount,
                request.getEmployeeId()
        );

        paymentRepository.markSuccess(paymentId, request.getEmployeeId());
        return new PaymentResponseDTO(
                paymentCode,
                request.getOrderCode(),
                amount,
                "SUCCESS",
                request.getBankName(),
                LocalDateTime.now()
        );
    }
}