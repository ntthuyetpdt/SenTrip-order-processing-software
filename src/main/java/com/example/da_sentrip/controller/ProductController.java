package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.reponse.ProductReponseDTO;
import com.example.da_sentrip.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/getAll")
    public SuccessResponse<?> getAll(){
        List<ProductReponseDTO> user =productService.getAll();
        return new SuccessResponse<>(
                200,
                "get all list product success",
                user
        );
    }
}
