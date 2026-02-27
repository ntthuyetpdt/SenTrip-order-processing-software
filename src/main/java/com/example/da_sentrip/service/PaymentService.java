package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.reponse.PaymentResponseDTO;
import com.example.da_sentrip.model.dto.request.PaymentRequestDTO;

public interface PaymentService {
        PaymentResponseDTO payOrder (PaymentRequestDTO request);

}
