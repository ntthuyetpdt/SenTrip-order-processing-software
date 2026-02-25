package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.ProductDTO;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.model.dto.request.ProductRequestDTO;
import com.example.da_sentrip.model.entity.Product;
import com.example.da_sentrip.repository.DataSourceRepository;
import com.example.da_sentrip.repository.ProductRepository;
import com.example.da_sentrip.service.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductServicelmpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final DataSourceRepository dataSourceRepository;
    private final MediaStorageService mediaStorageService;

    @Override
    public List<ProductReponseDTO> getAll() {
        return productRepository.findAll().stream().map(product -> {
            ProductReponseDTO dto =new ProductReponseDTO(product);
            if(product.getImg() !=null && product.getImg().matches("\\d+")){
                dataSourceRepository.findById(Long.valueOf(product.getImg())).ifPresent(ds -> {
                    dto.setImg(ds.getImageUrl());
                });
            }
            return dto;
        }).toList();
    }

    @Override
    public ProductDTO create(ProductRequestDTO request) {
        return new ProductDTO(modelMapper.map(request, Product.class));
    }

    @Override
    public ProductDTO update(Long id, ProductRequestDTO request, MultipartFile img) {
        Product product = productRepository.findById(id).orElseThrow(() -> new BadCredentialsException("ID NOT FOUND"));
        modelMapper.map(request, product);
        if (img != null && !img.isEmpty()) {
            if (product.getImg() != null && product.getImg().matches("\\d+")) {
                mediaStorageService.deleteMedia(Long.valueOf(product.getImg()));
            }
            String newDsId = mediaStorageService.uploadMedia(img);
            product.setImg(newDsId);
        }
        productRepository.save(product);
        return new ProductDTO(product);
    }

    @Override
    public ProductDTO delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow()
    }

    @Override
    public List<ProductReponseDTO> search(String productName, String price, String address) {
        return List.of();
    }
}
