package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.ProductDTO;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.model.dto.request.ProductRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductService {
    List<ProductReponseDTO> getAll ();
    ProductDTO create (ProductRequestDTO request);
    ProductDTO update (Long id, ProductRequestDTO request, MultipartFile img);
    ProductDTO delete (Long id);
    List<ProductReponseDTO> search (String productName,String price,String address);

}
