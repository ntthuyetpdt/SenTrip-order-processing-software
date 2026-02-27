//package com.example.da_sentrip.service.Impl;
//
//import com.example.da_sentrip.config.VnPayProperties;
//import com.example.da_sentrip.model.dto.reponse.CreateVnPayPaymentResponse;
//import com.example.da_sentrip.model.dto.reponse.PaymentResponseDTO;
//import com.example.da_sentrip.model.entity.Order;
//import com.example.da_sentrip.model.entity.Payment;
//import com.example.da_sentrip.model.enums.OrderStatus;
//import com.example.da_sentrip.repository.OrderRepository;
//import com.example.da_sentrip.repository.PaymentRepository;
//import com.example.da_sentrip.service.VnPayPaymentService;
//import com.example.da_sentrip.utils.VnPayUtil;
//import jakarta.transaction.Transactional;
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@AllArgsConstructor
//public class VnPayPaymentServiceImpl implements VnPayPaymentService {
//
//    private final VnPayProperties props;
//    private final OrderRepository orderRepository;
//    private final PaymentRepository paymentRepository;
//
//    @Override
//    @Transactional
//    public CreateVnPayPaymentResponse createPaymentUrl(String orderCode, String ipAddress) {
//        Order order = orderRepository.findByOrderCode(orderCode)
//                .orElseThrow(() -> new BadCredentialsException("ORDER NOT FOUND: " + orderCode));
//
//        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalStateException("INVALID ORDER AMOUNT");
//        }
//
//        long vnpAmount = order.getTotalAmount().longValueExact() * 100L;
//        String txnRef = order.getOrderCode();
//        String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//
//        Map<String, String> params = new HashMap<>();
//        params.put("vnp_Version", props.getVersion());
//        params.put("vnp_Command", props.getCommand());
//        params.put("vnp_TmnCode", props.getTmnCode());
//        params.put("vnp_Amount", String.valueOf(vnpAmount));
//        params.put("vnp_CurrCode", props.getCurrCode());
//        params.put("vnp_Locale", props.getLocale());
//        params.put("vnp_OrderType", props.getOrderType());
//        params.put("vnp_TxnRef", txnRef);
//        params.put("vnp_OrderInfo", "Thanh toan don hang " + txnRef);
//        params.put("vnp_ReturnUrl", props.getReturnUrl());
//        params.put("vnp_IpAddr", ipAddress);
//        params.put("vnp_CreateDate", createDate);
//
//        String hashData = VnPayUtil.buildHashData(params);
//        String secureHash = VnPayUtil.hmacSHA512(props.getHashSecret(), hashData);
//
//        params.put("vnp_SecureHash", secureHash);
//
//        String payUrl = props.getPayUrl() + "?" + VnPayUtil.buildQueryString(params);
//        return new CreateVnPayPaymentResponse(orderCode, payUrl);
//    }
//
//    @Override
//    @Transactional
//    public PaymentResponseDTO handleReturn(Map<String, String> allParams) {
//        verifySignature(allParams);
//
//        Map<String, String> vnp = VnPayUtil.extractVnpParams(allParams);
//
//        String orderCode = vnp.get("vnp_TxnRef");
//        String responseCode = vnp.get("vnp_ResponseCode");
//        String txnStatus = vnp.get("vnp_TransactionStatus");
//        String amountStr = vnp.get("vnp_Amount");
//
//        Order order = orderRepository.findByOrderCode(orderCode)
//                .orElseThrow(() -> new BadCredentialsException("ORDER NOT FOUND: " + orderCode));
//
//        boolean success = "00".equals(responseCode) && "00".equals(txnStatus);
//
//        Payment payment = upsertPayment(order, vnp, success, amountStr);
//
//        if (success) {
//            order.setOrderStatus(OrderStatus.COMPLETED);
//        } else {
//            order.setOrderStatus(OrderStatus.PENDING);
//        }
//        order.setUpdatedAt(LocalDateTime.now());
//        orderRepository.save(order);
//
//        return new PaymentResponseDTO(
//                payment.getPaymentCode(),
//                order.getOrderCode(),
//                payment.getAmount(),
//                payment.getPaymentStatus(),
//                payment.getBankName(),
//                payment.getPaidAt()
//        );
//    }
//
//    @Override
//    @Transactional
//    public String handleIpn(Map<String, String> allParams) {
//        verifySignature(allParams);
//
//        Map<String, String> vnp = VnPayUtil.extractVnpParams(allParams);
//
//        String orderCode = vnp.get("vnp_TxnRef");
//        String responseCode = vnp.get("vnp_ResponseCode");
//        String txnStatus = vnp.get("vnp_TransactionStatus");
//        String amountStr = vnp.get("vnp_Amount");
//
//        Order order = orderRepository.findByOrderCode(orderCode)
//                .orElseThrow(() -> new BadCredentialsException("ORDER NOT FOUND: " + orderCode));
//
//        boolean success = "00".equals(responseCode) && "00".equals(txnStatus);
//
//        upsertPayment(order, vnp, success, amountStr);
//
//        if (success) {
//            order.setOrderStatus(OrderStatus.COMPLETED);
//            order.setUpdatedAt(LocalDateTime.now());
//            orderRepository.save(order);
//        }
//
//        return "{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}";
//    }
//
//    private void verifySignature(Map<String, String> allParams) {
//        Map<String, String> vnp = new HashMap<>(VnPayUtil.extractVnpParams(allParams));
//
//        String receivedHash = vnp.remove("vnp_SecureHash");
//        vnp.remove("vnp_SecureHashType");
//
//        String hashData = VnPayUtil.buildHashData(vnp);
//        String expectedHash = VnPayUtil.hmacSHA512(props.getHashSecret(), hashData);
//
//        if (receivedHash == null || !expectedHash.equalsIgnoreCase(receivedHash)) {
//            throw new BadCredentialsException("INVALID SIGNATURE");
//        }
//    }
//
//    private Payment upsertPayment(Order order, Map<String, String> vnp, boolean success, String amountStr) {
//        String paymentCode = vnp.getOrDefault("vnp_TransactionNo", order.getOrderCode());
//
//        Payment payment = paymentRepository.findByPaymentCode(paymentCode)
//                .orElseGet(Payment::new);
//
//        payment.setPaymentCode(paymentCode);
//        payment.setOrder(order);
//
//        if (amountStr != null && !amountStr.isBlank()) {
//            long vnpAmount = Long.parseLong(amountStr);
//            payment.setAmount(BigDecimal.valueOf(vnpAmount).divide(BigDecimal.valueOf(100)));
//        } else {
//            payment.setAmount(order.getTotalAmount());
//        }
//
//        payment.setPaymentStatus(success ? "SUCCESS" : "FAILED");
//        payment.setBankName(vnp.get("vnp_BankCode"));
//        payment.setBankAccount(null);
//        payment.setPaidAt(LocalDateTime.now());
//
//        return paymentRepository.save(payment);
//    }
//}