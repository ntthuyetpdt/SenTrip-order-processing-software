package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.ProductDTO;
import com.example.da_sentrip.model.dto.reponse.ListProductMechartReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.ProductRequestDTO;
import com.example.da_sentrip.service.ProductService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/viewproduct")
    public SuccessResponse<?> getAll() {
        List<ProductReponseDTO> products = productService.getAll();
        return new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS,
                "view product success",
                products);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<?> create(
            @RequestPart(value = "request", required = false) ProductRequestDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Authentication authentication) {
        try {
            productService.create( request, img, authentication);
            return new SuccessResponse<>(200, "create success", null);
        } catch (Exception ex) {
            return new SuccessResponse<>(400, "create failed: " + ex.getMessage(), null);
        }
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<?> update(
            @PathVariable Long id,
            @RequestPart(value = "request") ProductRequestDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Authentication authentication) {
        try {
            productService.update( id,request, img, authentication);
            return new SuccessResponse<>(200, "update success", null);
        } catch (Exception ex) {
            return new SuccessResponse<>(400, "Update failed: " + ex.getMessage(), null);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        productService.delete(id);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("delete success")
                    .build()
            );
        }catch (Exception ex){
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.NOT_FOUND)
                    .message("delete failed")
                    .build()
            );
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO> search(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String address
    ) {
        List<ProductReponseDTO> view = productService.search(productName, price, address);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("search success")
                    .data(view)
                    .build()
            );
        }catch (Exception ex){
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.NOT_FOUND)
                    .message("search failed")
                    .build()
            );
        }

    }
    @GetMapping("/getOderCustome")
    public SuccessResponse<?> getMerchantOrders(Authentication authentication) {
        List<ListProductMechartReponseDTO> data = productService.getOrderCustomerFull(authentication);
        return new SuccessResponse<>(
                Constants.HTTP_STATUS.SUCCESS,
                "view success",
                data
        );
    }
}