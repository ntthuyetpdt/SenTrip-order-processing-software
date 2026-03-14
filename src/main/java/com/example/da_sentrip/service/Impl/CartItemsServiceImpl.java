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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        User user = getUser(authentication);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Carts cart = cartsRepository.findByUserId(user.getId()).orElseGet(() -> {
            Carts c = new Carts();
            c.setUserId(user.getId());
            c.setCreatedAt(LocalDateTime.now());
            return cartsRepository.save(c);
        });

        BigDecimal cartId = BigDecimal.valueOf(cart.getId());
        CartItems cartItems = cartItemsRepository.findByCartIdAndProductId_Id(cartId, request.getProductId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity().add(BigDecimal.valueOf(request.getQuantity())));
                    return existing;
                })
                .orElseGet(() -> {
                    CartItems c = new CartItems();
                    c.setCartId(cartId);
                    c.setProductId(product);
                    c.setQuantity(BigDecimal.valueOf(request.getQuantity()));
                    c.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
                    return c;
                });

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
        CartItems item = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new BadCredentialsException("Cart item not found"));
        cartItemsRepository.delete(item);
        return "Delete cart item successfully";
    }

    @Override
    public List<CartDetailResponseDTO> getCart(Authentication authentication) {
        User user = getUser(authentication);
        Optional<Carts> cartOpt = cartsRepository.findByUserId(user.getId());
        if (cartOpt.isEmpty()) return Collections.emptyList();

        return cartItemsRepository.findByCartId(BigDecimal.valueOf(cartOpt.get().getId())).stream()
                .map(item -> {
                    Product product = item.getProductId();
                    CartDetailResponseDTO dto = new CartDetailResponseDTO();
                    dto.setCartItemId(item.getId());
                    dto.setProductId(product.getId());
                    dto.setProductName(product.getProductName());
                    dto.setPrice(String.valueOf(product.getPrice()));
                    dto.setQuantity(item.getQuantity());
                    if (product.getImg() != null && product.getImg().matches("\\d+"))
                        dataSourceRepository.findById(Long.valueOf(product.getImg()))
                                .ifPresent(ds -> dto.setImg(ds.getImageUrl()));
                    else
                        dto.setImg(product.getImg());
                    return dto;
                })
                .toList();
    }

    private User getUser(Authentication authentication) {
        return userRepository.findByGmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}