package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.MerchantDTO;
import com.example.da_sentrip.model.dto.reponse.*;
import com.example.da_sentrip.model.dto.request.MerchantDashboardRequestDTO;
import com.example.da_sentrip.model.dto.request.MerchantRequestDTO;
import com.example.da_sentrip.service.MerchantService;
import com.example.da_sentrip.service.OrderService;
import com.example.da_sentrip.service.ProductService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<MerchantReponseDTO>>> getAll() {
        try {
            return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Get all success", merchantService.getAll()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(Constants.HTTP_STATUS.BAD_REQUEST, "Lấy danh sách thất bại: " + e.getMessage(), null));
        }
    }

    @PostMapping("/update/profile")
    @PreAuthorize("hasAnyAuthority('MERCHANT_UPDATE_PROFILE')")
    public ResponseEntity<ResponseDTO> update(
            @RequestPart(required = false)  MerchantRequestDTO request,
            @RequestParam(required = false) MultipartFile img,
            Authentication authentication) {
        try {
            String gmail = authentication.getName();
            merchantService.update(gmail, request, img);
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok").code(Constants.HTTP_STATUS.SUCCESS).message("update success").build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .status("error").code(Constants.HTTP_STATUS.BAD_REQUEST).message("update failed: " + e.getMessage()).build());
        }
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_DELETE_MERCHANT')")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        try {
            merchantService.delete(id);
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok").code(Constants.HTTP_STATUS.SUCCESS).message("delete success").build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .status("error").code(Constants.HTTP_STATUS.BAD_REQUEST).message("delete failed: " + e.getMessage()).build());
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN_SEARCH_MERCHANT')")
    public ResponseEntity<SuccessResponse<List<MerchantDTO>>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String businessLicense) {
        try {
            return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "search success", merchantService.search(fullName, address, businessLicense)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(Constants.HTTP_STATUS.BAD_REQUEST, "search failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/dashboard")
    @PreAuthorize("hasAnyAuthority('MERCHANT_DASHBOARD')")
    public ResponseEntity<MerchantDashboardResponseDTO> getDashboard(
            Authentication authentication,
            @RequestBody(required = false) MerchantDashboardRequestDTO request) {
        try {
            return ResponseEntity.ok(productService.getMerchantDashboard(authentication, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyAuthority('MERCHANT_VIEW_OWN')")
    public ResponseEntity<SuccessResponse<List<ProductReponseDTO>>> getMerchantProducts(Authentication authentication) {
        try {
            return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "view product success", productService.getMechant(authentication)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SuccessResponse<>(Constants.HTTP_STATUS.BAD_REQUEST, "view product failed: " + e.getMessage(), null));
        }
    }

    @GetMapping("/detail/{orderCode}")
    @PreAuthorize("hasAnyAuthority('MERCHANT_VIEW_DETAILS')")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable String orderCode) {
        try {
            return ResponseEntity.ok(orderService.getOrderDetail(orderCode));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyAuthority('MERCHANT_STATIC')")
    public ResponseEntity<List<ProductStatisticResponse>> getProductStatistic(Authentication authentication) {
        try {
            return ResponseEntity.ok(productService.getProductStatistic(authentication));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAnyAuthority('MERCHANT_REVENUE')")
    public ResponseEntity<ResponseDTO> getMerchantOrders(Authentication authentication) {
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok").code(Constants.HTTP_STATUS.SUCCESS)
                    .message("view success").data(productService.getOrderCustomerFull(authentication)).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder()
                    .status("error").code(Constants.HTTP_STATUS.BAD_REQUEST).message("view failed: " + e.getMessage()).build());
        }
    }

    @GetMapping("/searchOder")
    @PreAuthorize("hasAnyAuthority('MERCHANT_SEARCH_ORDER')")
    public ResponseEntity<ResponseDTO> searchByMerchant(
            Authentication authentication,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS)
                .message("Search success")
                .data(orderService.searchByMerchant(authentication, orderCode, productName, minPrice, maxPrice, startDate, endDate))
                .build());
    }
}