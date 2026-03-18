package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.reponse.PayoutRequestResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PayoutRequestService {
    void createPayoutRequest(String orderCode, String gmail);
    void processPayout(Long id, MultipartFile file, String gmail);
    List<PayoutRequestResponse> getMyPayoutRequests(String gmail);
    List<PayoutRequestResponse> getAllPayoutRequests();
}