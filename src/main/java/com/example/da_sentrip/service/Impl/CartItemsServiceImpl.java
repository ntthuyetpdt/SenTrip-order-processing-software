package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.model.dto.CartItemsDTO;
import com.example.da_sentrip.model.dto.reponse.CartDetailResponseDTO;
import com.example.da_sentrip.model.dto.request.AddToCartRequest;
import com.example.da_sentrip.model.entity.*;
import com.example.da_sentrip.repository.*;
import com.example.da_sentrip.service.CartItemsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartItemsServiceImpl implements CartItemsService {

    private final CartItemsRepository cartItemsRepository;
    private final CartsRepository cartsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DataSourceRepository dataSourceRepository;

    @Override
    public CartItemsDTO add(Authentication authentication, AddToCartRequest request) {

        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        Carts cart = cartsRepository.findByUserId(user.getId()).orElse(null);
        if (cart == null) {
            cart = new Carts();
            cart.setUserId(user.getId());
            cart.setCreatedAt(LocalDateTime.now());
            cart = cartsRepository.save(cart);
        }

        BigDecimal cartId = BigDecimal.valueOf(cart.getId());
        CartItems cartItems = cartItemsRepository.findByCartIdAndProductId_Id(cartId, request.getProductId()).orElse(null);
        if (cartItems != null) {
            cartItems.setQuantity(cartItems.getQuantity().add(BigDecimal.valueOf(request.getQuantity())));
        } else {
            cartItems = new CartItems();
            cartItems.setCartId(cartId);
            cartItems.setProductId(product);
            cartItems.setQuantity(BigDecimal.valueOf(request.getQuantity()));
            cartItems.setPrice(new BigDecimal(product.getPrice())
                    .multiply(BigDecimal.valueOf(request.getQuantity())));
        }
        CartItems saved = cartItemsRepository.save(cartItems);
        CartItemsDTO dto = new CartItemsDTO();
        dto.setId(saved.getId());
        dto.setCartId(saved.getCartId());
        dto.setProductId(saved.getProductId().getId());
        dto.setQuantity(saved.getQuantity());
        dto.setPrice(saved.getPrice());
        return dto;
    }

    @Override
    public String delete(Long cartItemId) {
        CartItems cartItems = cartItemsRepository.findById(cartItemId).orElseThrow(() -> new BadCredentialsException("CART ITEM ID NOT FOUND"));
        cartItemsRepository.delete(cartItems);
        return "Delete cart item successfully";
    }

    @Override
    public List<CartDetailResponseDTO> getCart(Authentication authentication) {
        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        Carts cart = cartsRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItems> cartItemsList = cartItemsRepository.findByCartId(BigDecimal.valueOf(cart.getId()));
        return cartItemsList.stream().map(item -> {
            Product product = item.getProductId();
            CartDetailResponseDTO dto = new CartDetailResponseDTO();
            if (product.getImg() != null && product.getImg().matches("\\d+")) {
                dataSourceRepository.findById(Long.valueOf(product.getImg())).ifPresent(ds -> {
                    dto.setImg(ds.getImageUrl());
                });
            } else {
                dto.setImg(product.getImg());
            }
            dto.setCartItemId(item.getId());
            dto.setProductId(product.getId());
            dto.setProductName(product.getProductName());
            dto.setPrice(product.getPrice());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).collect (Collectors.toList());
    }
}