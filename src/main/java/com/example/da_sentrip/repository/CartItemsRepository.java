package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    Optional<CartItems> findByCartIdAndProductId_Id(BigDecimal cartId, Long productId);

    List<CartItems> findByCartId(BigDecimal cartId);
}