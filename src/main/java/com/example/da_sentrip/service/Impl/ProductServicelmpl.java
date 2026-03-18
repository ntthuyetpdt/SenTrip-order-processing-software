package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.exception.ResourceNotFoundException;
import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.ProductDTO;
import com.example.da_sentrip.model.dto.reponse.ListProductMechartReponseDTO;
import com.example.da_sentrip.model.dto.reponse.MerchantDashboardResponseDTO;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ProductStatisticResponse;
import com.example.da_sentrip.model.dto.reponse.view.MerchantDashboardView;
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
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
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

    private Merchant getMerchantFromAuth(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByGmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        return merchantRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Bạn chưa đăng ký profile Merchant"));
    }

    @Override
    public List<ProductReponseDTO> getAll() {
        return productRepository.findAll().stream().map(product -> {
            ProductReponseDTO dto = new ProductReponseDTO(product);
            if (product.getImg() != null && product.getImg().matches("\\d+")) {
                dataSourceRepository.findById(Long.valueOf(product.getImg()))
                        .ifPresent(ds -> dto.setImg(ds.getImageUrl()));
            }
            return dto;
        }).toList();
    }

    @Transactional
    @Override
    public ProductDTO create(ProductRequestDTO request, MultipartFile img, Authentication authentication) {
        Merchant merchant = getMerchantFromAuth(authentication);
        Product product = modelMapper.map(request, Product.class);
        product.setMerchant(merchant);
        product.setCreatedAt(LocalDateTime.now());
        if (img != null && !img.isEmpty()) {
            product.setImg(mediaStorageService.uploadMedia(img));
        }
        return new ProductDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO update(Long id, ProductRequestDTO request, MultipartFile img, Authentication authentication) {
        Merchant merchant = getMerchantFromAuth(authentication);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (!product.getMerchant().getId().equals(merchant.getId())) {
            throw new RuntimeException("You are not allowed to update this product");
        }
        modelMapper.map(request, product);
        if (img != null && !img.isEmpty()) {
            product.setImg(mediaStorageService.uploadMedia(img));
        }
        return new ProductDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("ID NOT FOUND"));
        return new ProductDTO(product);
    }

    @Override
    public List<ProductReponseDTO> search(String productName, BigDecimal minPrice, BigDecimal maxPrice, String address) {
        return productRepository.search(productName, minPrice, maxPrice, address).stream()
                .map(ProductReponseDTO::new)
                .toList();
    }

    @Override
    public List<ListProductMechartReponseDTO> getOrderCustomerFull(Authentication authentication) {
        Merchant merchant = getMerchantFromAuth(authentication);
        return productRepository.getOrderCustomerFull(merchant.getId()).stream()
                .map(view -> new ListProductMechartReponseDTO(
                        view.getOrderCode(), view.getCreatedAt(), view.getFullName(),
                        view.getPhone(), view.getProductName(), view.getType(),
                        view.getAdditionalServices(), view.getAddress(),
                        view.getQuantity(), view.getPrice()
                ))
                .toList();
    }

    @Override
    public MerchantDashboardResponseDTO getMerchantDashboard(Authentication authentication, MerchantDashboardRequestDTO request) {
        Merchant merchant = getMerchantFromAuth(authentication);

        LocalDateTime start = (request == null || request.getStartDate() == null)
                ? LocalDateTime.of(2000, 1, 1, 0, 0) : request.getStartDate();
        LocalDateTime end = (request == null || request.getEndDate() == null)
                ? LocalDateTime.now().plusYears(100) : request.getEndDate();

        MerchantDashboardView view = productRepository.getMerchantDashboard(merchant.getId(), start, end);
        return new MerchantDashboardResponseDTO(
                view.getTotalRevenue() != null ? view.getTotalRevenue() : BigDecimal.ZERO,
                view.getTotalCustomers() != null ? view.getTotalCustomers() : 0L,
                view.getTotalOrders() != null ? view.getTotalOrders() : 0L,
                view.getSuccessOrders() != null ? view.getSuccessOrders() : 0L,
                view.getCancelledOrders() != null ? view.getCancelledOrders() : 0L
        );
    }

    @Override
    public List<ProductReponseDTO> getMechant(Authentication authentication) {
        Merchant merchant = getMerchantFromAuth(authentication);
        return productRepository.findByMerchantId(merchant.getId()).stream().map(product -> {
            ProductReponseDTO dto = new ProductReponseDTO(product);
            if (product.getImg() != null && product.getImg().matches("\\d+")) {
                dataSourceRepository.findById(Long.valueOf(product.getImg()))
                        .ifPresent(ds -> dto.setImg(ds.getImageUrl()));
            }
            return dto;
        }).toList();
    }

    @Override
    public List<ProductStatisticResponse> getProductStatistic(Authentication authentication) {
        Merchant merchant = getMerchantFromAuth(authentication);
        return productRepository.findProductStatistic(merchant.getId()).stream()
                .map(view -> new ProductStatisticResponse(
                        view.getProductId(), view.getProductName(), view.getAdditionalServices(),
                        view.getTotalCustomers(), view.getTotalOrders(), view.getTotalRevenue()
                ))
                .toList();
    }

    public List<ProductStatisticResponse> getProductStatisticall() {
        return productRepository.findProductStatistic().stream()
                .map(p -> new ProductStatisticResponse(
                        p.getProductId(),
                        p.getProductName(),
                        p.getAdditionalServices(),
                        p.getTotalCustomers(),
                        p.getTotalOrders(),
                        p.getTotalRevenue()
                ))
                .toList();
    }
}
