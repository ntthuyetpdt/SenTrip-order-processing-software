package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.MerchantDTO;
import com.example.da_sentrip.model.dto.reponse.MerchantReponseDTO;
import com.example.da_sentrip.model.dto.request.MerchantRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface MerchantService {
    MerchantDTO create (MerchantRequestDTO request , Long id);
    MerchantDTO update (Long id, MerchantRequestDTO request, MultipartFile img);
    MerchantDTO delete (Long id);
    List<MerchantDTO> getdetailis(Long id);
    List<MerchantDTO> search(String merchantName, String address, String businessLicense);
    List<MerchantReponseDTO> getAll ( );

}
