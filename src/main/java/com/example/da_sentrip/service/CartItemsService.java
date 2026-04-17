package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.CartItemsDTO;
import com.example.da_sentrip.model.dto.reponse.CartDetailResponseDTO;
import com.example.da_sentrip.model.dto.request.AddToCartRequest;
import com.example.da_sentrip.model.dto.request.CartItemRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CartItemsService {
    CartItemsDTO add(Authentication authentication, AddToCartRequest request);
    String delete(Long cartItemId);
    List<CartDetailResponseDTO> getCart(Authentication authentication);
    void  update (Long id, CartItemRequest request);
}