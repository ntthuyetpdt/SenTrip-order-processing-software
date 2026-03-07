package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.reponse.CartDetailResponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.AddToCartRequest;
import com.example.da_sentrip.service.CartItemsService;
import com.example.da_sentrip.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartItemsController {

    private final CartItemsService cartItemsService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addToCart( Authentication authentication,@RequestBody AddToCartRequest request) {
        cartItemsService.add(authentication,request);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("Add employee success")
                    .build());
        }catch (Exception ex){
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.BAD_REQUEST)
                    .message("Add employee success")
                    .build());
        }
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ResponseDTO> deleteCartItem(@PathVariable Long cartItemId) {
         cartItemsService.delete(cartItemId);
         try {
             return ResponseEntity.ok(ResponseDTO.builder()
                     .status("ok")
                     .code(Constants.HTTP_STATUS.SUCCESS)
                     .message("Delete customersuccess")
                     .build());
         }catch (Exception ex){
             return ResponseEntity.ok(ResponseDTO.builder()
                     .status("ok")
                     .code(Constants.HTTP_STATUS.BAD_REQUEST)
                     .message("Delete not success")
                     .build());
         }
    }

    @GetMapping("/my-cart")
    public ResponseEntity<ResponseDTO> getMyCart(Authentication authentication) {
        List<CartDetailResponseDTO> cart =cartItemsService.getCart(authentication);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("view  cart success")
                    .data(cart)
                    .build());
        }catch (Exception ex) {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.UNAUTHORIZED)
                    .message("view  cart not success")
                    .data(null)
                    .build());
        }
    }
}