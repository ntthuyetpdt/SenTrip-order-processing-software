package com.example.da_sentrip.service;


import com.example.da_sentrip.model.dto.CustomerDTO;
import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.dto.reponse.CustomerReponseDTO;
import com.example.da_sentrip.model.dto.reponse.EmployeeReponseDTO;
import com.example.da_sentrip.model.dto.request.CustomerRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CustomerService {
    CustomerDTO create (CustomerRequestDTO request , Long id);
    CustomerDTO update (Long id, CustomerRequestDTO request, MultipartFile img);
    CustomerDTO delete (Long id);
    List<CustomerDTO> search(String fullName, String address, String phone);
    List<CustomerReponseDTO> getAll ( );
}
