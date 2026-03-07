package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.ProductDTO;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.ProductRequestDTO;
import com.example.da_sentrip.service.ProductService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> create(@RequestBody ProductRequestDTO request) {
        ProductDTO product = productService.create(request);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.CREATED)
                    .message("create  success")
                    .data(product)
                    .build());
        }catch (Exception ex) {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.BAD_REQUEST)
                    .message("create  failed")
                    .build());
        }
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> update(
            @PathVariable Long id,
            @RequestPart(value = "request", required = false) ProductRequestDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img
    ) {
        if (request == null) request = new ProductRequestDTO();
        productService.update(id, request, img);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("create  success")
                    .build());
        }catch (Exception ex) {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.BAD_REQUEST)
                    .message("create  failed")
                    .build());
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
}