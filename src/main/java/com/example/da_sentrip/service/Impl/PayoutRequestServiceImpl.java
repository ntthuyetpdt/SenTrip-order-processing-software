package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.reponse.PayoutRequestResponse;
import com.example.da_sentrip.model.entity.Merchant;
import com.example.da_sentrip.model.entity.PayoutRequest;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.repository.*;
import com.example.da_sentrip.service.PayoutRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayoutRequestServiceImpl implements PayoutRequestService {

    private final PayoutRequestRepository payoutRequestRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final MediaStorageService mediaStorageService;
    private final DataSourceRepository dataSourceRepository;

    @Override
    @Transactional
    public void createPayoutRequest(String orderCode, String gmail) {
        User user = userRepository.findByGmail(gmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Merchant merchant = merchantRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Merchant not found"));

        if (payoutRequestRepository.existsByOrderCode(orderCode)) {
            throw new RuntimeException("Yêu cầu cho đơn hàng này đã tồn tại");
        }

        var order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderCode));

        BigDecimal orderAmount = order.getTotalAmount();
        BigDecimal payoutAmount = orderAmount
                .multiply(BigDecimal.valueOf(85))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        PayoutRequest request = new PayoutRequest();
        request.setOrderCode(orderCode);
        request.setMerchant(merchant);
        request.setOrderAmount(orderAmount);
        request.setPayoutAmount(payoutAmount);
        request.setStatus("PENDING");
        request.setRequestedAt(LocalDateTime.now());

        payoutRequestRepository.save(request);
    }

    @Override
    @Transactional
    public void processPayout(Long id, MultipartFile file, String gmail) {
        User user = userRepository.findByGmail(gmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PayoutRequest request = payoutRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Payout request not found: " + id));

        String mediaId = mediaStorageService.uploadMedia(file);
        String fileUrl = dataSourceRepository.findById(Long.valueOf(mediaId))
                .orElseThrow(() -> new RuntimeException("Media not found"))
                .getImageUrl();

        request.setStatus("PAID");
        request.setBillFileName(file.getOriginalFilename());
        request.setBillFileUrl(fileUrl);
        request.setProcessedAt(LocalDateTime.now());
        request.setProcessedBy(user.getId());
        payoutRequestRepository.save(request);
    }

    @Override
    public List<PayoutRequestResponse> getMyPayoutRequests(String gmail) {
        User user = userRepository.findByGmail(gmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Merchant merchant = merchantRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Merchant not found"));

        List<PayoutRequest> list = payoutRequestRepository.findByMerchant(merchant);
        System.out.println(">>> list size: " + list.size());

        return list.stream()
                .map(this::toResponse)
                .toList();
    }
    @Override
    public List<PayoutRequestResponse> getAllPayoutRequests() {
        return payoutRequestRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private PayoutRequestResponse toResponse(PayoutRequest p) {
        return new PayoutRequestResponse(
                p.getId(),
                p.getOrderCode(),
                p.getMerchant().getId(),
                p.getMerchant().getMerchantName(),
                p.getMerchant().getBankName(),
                p.getMerchant().getBankAccount(),
                p.getOrderAmount(),
                p.getPayoutAmount(),
                p.getStatus(),
                p.getBillFileUrl(),
                p.getRequestedAt(),
                p.getProcessedAt()
        );
    }
}