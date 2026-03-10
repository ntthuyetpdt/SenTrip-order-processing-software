package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.ProductDTO;
import com.example.da_sentrip.model.dto.reponse.ListProductMechartReponseDTO;
import com.example.da_sentrip.model.dto.reponse.MerchantDashboardResponseDTO;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.model.dto.request.MerchantDashboardRequestDTO;
import com.example.da_sentrip.model.dto.request.ProductRequestDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductService {
    List<ProductReponseDTO> getAll ();
    ProductDTO create (ProductRequestDTO request,MultipartFile img, Authentication authentication);
    ProductDTO update (Long id, ProductRequestDTO request, MultipartFile img);
    ProductDTO delete (Long id);
    List<ProductReponseDTO> search (String productName,String price,String address);
    List<ListProductMechartReponseDTO> getOrderCustomerFull(Authentication authentication);
    MerchantDashboardResponseDTO getMerchantDashboard(Authentication authentication, MerchantDashboardRequestDTO request) ;
    List<ProductReponseDTO>getMechant(Authentication authentication);
}
