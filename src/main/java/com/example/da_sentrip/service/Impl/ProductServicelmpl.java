package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.exception.ResourceNotFoundException;
import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.ProductDTO;
import com.example.da_sentrip.model.dto.reponse.ListProductMechartReponseDTO;
import com.example.da_sentrip.model.dto.reponse.MerchantDashboardResponseDTO;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.model.dto.reponse.view.MerchantDashboardView;
import com.example.da_sentrip.model.dto.reponse.view.OrderCustomerView;
import com.example.da_sentrip.model.dto.request.MerchantDashboardRequestDTO;
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
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    public MerchantDashboardResponseDTO getMerchantDashboard(Authentication authentication, MerchantDashboardRequestDTO request) {
        String email = authentication.getName();
        User user = userRepository.findByGmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        Merchant merchant = merchantRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Bạn chưa đăng ký profile Merchant"));
        LocalDateTime finalStart;
        LocalDateTime finalEnd;

        if (request == null || (request.getStartDate() == null && request.getEndDate() == null)) {
            finalStart = LocalDateTime.of(2000, 1, 1, 0, 0);
            finalEnd = LocalDateTime.now().plusYears(100);
        } else {
            finalStart = (request.getStartDate() != null) ? request.getStartDate() : LocalDateTime.of(1970, 1, 1, 0, 0);
            finalEnd = (request.getEndDate() != null) ? request.getEndDate() : LocalDateTime.now().plusYears(100);
        }
        MerchantDashboardView view = productRepository.getMerchantDashboard(
                merchant.getId(),
                finalStart,
                finalEnd
        );
        return new MerchantDashboardResponseDTO(
                view.getTotalRevenue() != null ? view.getTotalRevenue() : BigDecimal.ZERO,
                view.getTotalCustomers() != null ? view.getTotalCustomers() : 0L,
                view.getTotalOrders() != null ? view.getTotalOrders() : 0L
        );
    }

    @Override
    public List<ProductReponseDTO> getMechant(Authentication authentication) {
        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        Merchant merchant = merchantRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Merchant not found"));
        return  productRepository.findByMerchantId(merchant.getId()).stream().map(product -> {
            ProductReponseDTO dto = new ProductReponseDTO(product);
            if (product.getImg() != null && product.getImg().matches("\\d+")) {
                dataSourceRepository.findById(Long.valueOf(product.getImg())).ifPresent(ds -> {
                    dto.setImg(ds.getImageUrl());
                });
            }
            return dto;
        }).toList();
    }
}
