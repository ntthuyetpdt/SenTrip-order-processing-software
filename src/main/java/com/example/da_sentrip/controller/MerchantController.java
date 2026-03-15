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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Get all success", merchantService.getAll()));
    }

    @PostMapping("update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_VIEW_INVOICE')")
    public ResponseEntity<ResponseDTO> update(@PathVariable Long id, @ModelAttribute MerchantRequestDTO request, MultipartFile img) {
        merchantService.update(id, request, img);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS).message("Update success").build());
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_DELETE_MERCHANT')")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        merchantService.delete(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS).message("Delete success").build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN_SEARCH_MERCHANT')")
    public ResponseEntity<SuccessResponse<List<MerchantDTO>>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String mnv) {
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Search success", merchantService.search(fullName, address, mnv)));
    }

    @PostMapping("/dashboard")
    @PreAuthorize("hasAnyAuthority('MERCHANT_DASHBOARD')")
    public ResponseEntity<MerchantDashboardResponseDTO> getDashboard(
            Authentication authentication,
            @RequestBody(required = false) MerchantDashboardRequestDTO request) {
        return ResponseEntity.ok(productService.getMerchantDashboard(authentication, request));
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyAuthority('MERCHANT_VIEW_OWN')")
    public ResponseEntity<SuccessResponse<List<ProductReponseDTO>>> getMerchantProducts(Authentication authentication) {
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Get products success", productService.getMechant(authentication)));
    }
    @GetMapping("/detail/{orderCode}")
    @PreAuthorize("hasAnyAuthority('MERCHANT_VIEW_DETAILS')")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable String orderCode) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderCode));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyAuthority('MERCHANT_STATIC')")
    public ResponseEntity<List<ProductStatisticResponse>> getProductStatistic(Authentication authentication) {
        return ResponseEntity.ok(productService.getProductStatistic(authentication));
    }
    @GetMapping("/orders")
    @PreAuthorize("hasAnyAuthority('MERCHANT_REVENUE')")
    public ResponseEntity<ResponseDTO> getMerchantOrders(Authentication authentication) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get orders success").data(productService.getOrderCustomerFull(authentication)).build());
    }
}