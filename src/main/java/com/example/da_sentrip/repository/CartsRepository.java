package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartsRepository extends JpaRepository<Carts, Long> {
    Optional<Carts> findByUserId(Long userId);
}