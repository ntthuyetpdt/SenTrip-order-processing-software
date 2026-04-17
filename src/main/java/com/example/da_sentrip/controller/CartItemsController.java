package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.AddToCartRequest;
import com.example.da_sentrip.model.dto.request.CartItemRequest;
import com.example.da_sentrip.service.CartItemsService;
import com.example.da_sentrip.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartItemsController {

    private final CartItemsService cartItemsService;

    @PostMapping("/oder")
    @PreAuthorize("hasAuthority('CUSTOMER_CART_PRODUCT')")
    public ResponseEntity<ResponseDTO> addToCart(Authentication authentication, @RequestBody AddToCartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.CREATED)
                .message("Add to cart success")
                .data(cartItemsService.add(authentication, request))
                .build());
    }

    @PostMapping("delete/{cartItemId}")
    @PreAuthorize("hasAuthority('CUSTOMER_DELETE_CART')")
    public ResponseEntity<ResponseDTO> deleteCartItem(@PathVariable Long cartItemId) {
        cartItemsService.delete(cartItemId);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Delete cart item success")
                .build());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('CUSTOMER_CART')")
    public ResponseEntity<ResponseDTO> getMyCart(Authentication authentication) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get cart success")
                .data(cartItemsService.getCart(authentication))
                .build());
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CartItemRequest request) {
        cartItemsService.update(id, request);
        return ResponseEntity.ok("update success");
    }
}