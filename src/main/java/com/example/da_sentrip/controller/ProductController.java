package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.ProductDTO;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.model.dto.request.ProductRequestDTO;
import com.example.da_sentrip.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/getAll")
    public SuccessResponse<?> getAll() {
        List<ProductReponseDTO> products = productService.getAll();
        return new SuccessResponse<>(200, "Get all list product success", products);
    }

    @PostMapping("/create")
    public SuccessResponse<?> create(@RequestBody ProductRequestDTO request) {
        ProductDTO product = productService.create(request);
        return new SuccessResponse<>(200, "Create product success", product);
    }

    @PutMapping("/update/{id}")
    public SuccessResponse<?> update(
            @PathVariable Long id,
            @ModelAttribute ProductRequestDTO request, // Dùng ModelAttribute để nhận form-data kèm file
            @RequestParam(value = "img", required = false) MultipartFile img
    ) {
        ProductDTO product = productService.update(id, request, img);
        return new SuccessResponse<>(200, "Update product success", product);
    }

    @DeleteMapping("/delete/{id}")
    public SuccessResponse<?> delete(@PathVariable Long id) {
        ProductDTO product = productService.delete(id);
        return new SuccessResponse<>(200, "Delete product success", product);
    }

    @GetMapping("/search")
    public SuccessResponse<?> search(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String address
    ) {
        List<ProductReponseDTO> results = productService.search(productName, price, address);
        return new SuccessResponse<>(200, "Search products success", results);
    }
}