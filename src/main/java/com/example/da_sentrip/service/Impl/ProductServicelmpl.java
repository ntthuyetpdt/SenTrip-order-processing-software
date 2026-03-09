package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.ProductDTO;
import com.example.da_sentrip.model.dto.reponse.ListProductMechartReponseDTO;
import com.example.da_sentrip.model.dto.reponse.MerchantDashboardResponseDTO;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.model.dto.reponse.view.MerchantDashboardView;
import com.example.da_sentrip.model.dto.reponse.view.OrderCustomerView;
import com.example.da_sentrip.model.dto.request.ProductRequestDTO;
import com.example.da_sentrip.model.entity.Merchant;
import com.example.da_sentrip.model.entity.Product;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.repository.DataSourceRepository;
import com.example.da_sentrip.repository.MerchantRepository;
import com.example.da_sentrip.repository.ProductRepository;
import com.example.da_sentrip.repository.UserRepository;
import com.example.da_sentrip.security.JwtUtil;
import com.example.da_sentrip.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductServicelmpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final DataSourceRepository dataSourceRepository;
    private final MediaStorageService mediaStorageService;
    private final MerchantRepository merchantRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public List<ProductReponseDTO> getAll() {
        return productRepository.findAll().stream().map(product -> {
            ProductReponseDTO dto = new ProductReponseDTO(product);
            if (product.getImg() != null && product.getImg().matches("\\d+")) {
                dataSourceRepository.findById(Long.valueOf(product.getImg())).ifPresent(ds -> {
                    dto.setImg(ds.getImageUrl());
                });
            }
            return dto;
        }).toList();
    }

    @Transactional
    @Override
    public ProductDTO create(ProductRequestDTO request, MultipartFile img, Authentication authentication) {

        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        Merchant merchant = merchantRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Merchant not found"));
        Product product = modelMapper.map(request, Product.class);
        product.setMerchant(merchant);
        product.setCreatedAt(LocalDateTime.now());
        if (img != null && !img.isEmpty()) {
            String dsId = mediaStorageService.uploadMedia(img);
            product.setImg(dsId);
        }
        Product saved = productRepository.save(product);
        return new ProductDTO(saved);
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
        Product product = productRepository.findById(id).orElseThrow(() -> new BadCredentialsException("ID NOT FOUND"));
        return new ProductDTO(product);

    }

    @Override
    public List<ProductReponseDTO> search(String productName, String price, String address) {
        return productRepository.search(productName, price, address).stream().map(ProductReponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<ListProductMechartReponseDTO> getOrderCustomerFull(Authentication authentication) {
        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        Merchant merchant = merchantRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Merchant not found"));
        return productRepository.getOrderCustomerFull(merchant.getId())
                .stream()
                .map(view -> new ListProductMechartReponseDTO(
                        view.getOrderCode(),
                        view.getCreatedAt(),
                        view.getFullName(),
                        view.getPhone(),
                        view.getProductName(),
                        view.getType(),
                        view.getAdditionalServices(),
                        view.getAddress(),
                        view.getQuantity(),
                        view.getPrice()
                ))
                .toList();
    }

    @Override
    public List<MerchantDashboardResponseDTO> getMerchantDashboard(Authentication authentication) {
        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found with email: " + gmail));
        Merchant merchant = merchantRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Merchant profile not found for user: " + user.getId()));
        return  productRepository.getMerchantDashboard(merchant.getId()).stream().map(view -> new MerchantDashboardResponseDTO(
                view.getTotalRevenue(),
                view.getTotalCustomers(),
                view.getTotalOrders()
        )).toList();
    }
}
