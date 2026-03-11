package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.model.dto.OrderDTO;
import com.example.da_sentrip.model.dto.reponse.OderdetailReponseDTO;
import com.example.da_sentrip.model.dto.reponse.OrderAllReponseDTO;
import com.example.da_sentrip.model.dto.reponse.OrderReponseDTO;
import com.example.da_sentrip.model.dto.reponse.UserOrderDTO;
import com.example.da_sentrip.model.dto.reponse.view.OderDetailProjection;
import com.example.da_sentrip.model.dto.request.OrderRequestDTO;
import com.example.da_sentrip.model.entity.*;
import com.example.da_sentrip.model.enums.OrderStatus;
import com.example.da_sentrip.repository.*;
import com.example.da_sentrip.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;

    @Override
    @Transactional
    public OrderDTO Order(OrderRequestDTO request, String gmail) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("No product found.");
        }
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new BadCredentialsException("User not found"));
        Merchant merchant = request.getMerchantId() == null ? null : merchantRepository.findById(request.getMerchantId()).orElseThrow(() -> new BadCredentialsException("MERCHANT NOT FOUND"));
        Order order = new Order();
        order.setOrderCode(generateOrderCode());
        order.setUser(user);
        order.setMerchant(merchant);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        BigDecimal totalAmount = request.getItems().stream().map(item -> {
        Product product = productRepository.findById(item.getProductId()).orElseThrow(() -> new BadCredentialsException("PRODUCT NOT FOUND: " + item.getProductId()));
        if (product.getStatus() == null || product.getStatus() != 1)
            throw new IllegalStateException("PRODUCT IS INACTIVE: " + product.getId());
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        request.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProductId()).get();
            orderRepository.insertOrderItem(savedOrder.getId(), product.getId(), item.getQuantity(), String.valueOf(product.getPrice()));
        });
        return  mapToDTO(savedOrder);
    }

    @Override
    public List<OrderAllReponseDTO> Getall(Authentication authentication) {
        return orderRepository.findAllOrderSummary()
                .stream()
                .map(view -> new OrderAllReponseDTO(
                        view.getOrderCode(),
                        view.getFullNameCustomer(),
                        view.getCreatedAt(),
                        view.getServiceType(),
                        view.getAdditionalService(),
                        view.getAddress(),
                        view.getTotalAmount(),
                        view.getOrderStatus(),
                        view.getPaymentStatus()
                ))
                .toList();
    }


    @Override
    @Transactional
    public OrderDTO Cancel(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode).orElseThrow(() -> new BadCredentialsException("ORDER NOT FOUND: " + orderCode));
        orderRepository.deleteOrderItemsByOrderCode(orderCode);
        orderRepository.deleteOrderByOrderCode(orderCode);
        OrderDTO dto = mapToDTO(order);
        dto.setOrderStatus(OrderStatus.CANCELLED);
        return dto;
    }

    @Override
    public List<OrderReponseDTO> getMyOrders(Authentication authentication) {
        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findOrderSummaryByUser(user.getId()).stream().map(view -> {
                    OrderReponseDTO dto = new OrderReponseDTO(
                            view.getOrderCode(),
                            OrderStatus.valueOf(view.getOrderStatus()),
                            view.getTotalAmount(),
                            view.getCreatedAt(),
                            view.getProductNames(),
                            view.getAdditionalService(),
                            view.getImg(),
                            view.getQuantities()
                    );
                    return dto;
                }).toList();
    }

    @Override
    public List<OderdetailReponseDTO> details(String orderCode) {

        List<OderDetailProjection> projections = orderRepository.findOrderDetailByOrderCode(orderCode);
        List<OderdetailReponseDTO> result = new ArrayList<>();
        for (OderDetailProjection p : projections) {
            OderdetailReponseDTO dto = new OderdetailReponseDTO();
            dto.setCreatedAt(p.getCreatedAt());
            dto.setOrderCode(p.getOrderCode());
            dto.setOrderStatus(p.getOrderStatus());
            dto.setTotalAmount(p.getTotalAmount());
            dto.setUserId(p.getUserId());
            dto.setGmail(p.getGmail());
            dto.setFullName(p.getFullName());
            dto.setPhone(p.getPhone());
            dto.setCccd(p.getCccd());
            dto.setPaymentStatus(p.getPaymentStatus());
            dto.setPaymentCode(p.getPaymentCode());
            dto.setPaidAt(p.getPaidAt());
            dto.setProductNames(p.getProductNames());
            dto.setType(p.getType());
            dto.setServiceType(p.getServiceType());
            dto.setAdditionalService(p.getAdditionalService());
            dto.setDescription(p.getDescription());
            dto.setQuantities(p.getQuantities());
            dto.setImg(p.getImg());

            result.add(dto);
        }

        return result;
    }


    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderCode(order.getOrderCode());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUser(new UserOrderDTO(order.getUser()));
        return dto;
    }

    private String generateOrderCode() {
        String date = java.time.LocalDate.now().toString().replace("-", "");
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "OD-" + date + "-" + rand;
    }
}