package com.example.da_sentrip.service;


import com.example.da_sentrip.model.dto.CustomerDTO;
import com.example.da_sentrip.model.dto.reponse.CustomerReponseDTO;
import com.example.da_sentrip.model.dto.reponse.TicketReponseDTO;
import com.example.da_sentrip.model.dto.request.CustomerRequestDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CustomerService {
    CustomerDTO update (Long id, CustomerRequestDTO request, MultipartFile img, Authentication authentication);
    List<CustomerDTO> search(String fullName, String address, String phone);
    List<CustomerReponseDTO> getAll ( );
    List<TicketReponseDTO> getTicket (Authentication authentication);
}
