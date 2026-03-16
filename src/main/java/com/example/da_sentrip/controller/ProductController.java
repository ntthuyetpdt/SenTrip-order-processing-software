package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.reponse.ProductStatisticResponse;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.ProductRequestDTO;
import com.example.da_sentrip.service.ProductService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER_VIEW_PRODUCT')")
    public ResponseEntity<ResponseDTO> getAll() {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get all success").data(productService.getAll()).build());
    }

    @PostMapping(value = "create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('MERCHANT_CREATE_PRODUCT')")
    public ResponseEntity<ResponseDTO> create(
            @RequestPart(required = false) ProductRequestDTO request,
            @RequestPart(required = false) MultipartFile img,
            Authentication authentication) {
        productService.create(request, img, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.CREATED)
                .message("Create success").build());
    }

    @PostMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('MERCHANT_UPDATE_PRODUCT')")
    public ResponseEntity<ResponseDTO> update(
            @PathVariable Long id,
            @RequestPart ProductRequestDTO request,
            @RequestPart(required = false) MultipartFile img,
            Authentication authentication) {
        productService.update(id, request, img, authentication);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS)
                .message("Update success").build());
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('MERCHANT_DELETE_PRODUCT')")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS)
                .message("Delete success").build());
    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_SEARCH_PRODUCT','MERCHANT_SEARCH_PRODUCT')")
    public ResponseEntity<ResponseDTO> search(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String address) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS)
                .message("Search success").data(productService.search(productName, price, address)).build());
    }
    @GetMapping("/statistic")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE_VIEW_STATIC')")
    public ResponseEntity<?> getProductStatistic(Authentication authentication) {
        List<ProductStatisticResponse> data = productService.getProductStatisticall();
        return ResponseEntity.ok(Map.of(
                "message", "View success",
                "data", data
        ));
    }

}