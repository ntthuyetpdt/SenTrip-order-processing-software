package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
        SELECT * FROM product p
        WHERE (:productName IS NULL OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', :productName, '%')))
          AND (:address IS NULL OR LOWER(p.address) LIKE LOWER(CONCAT('%', :address, '%')))
          AND (:price IS NULL OR p.price = :price)
    """, nativeQuery = true)
    List<Product> search(
            @Param("productName") String productName,
            @Param("price") String price,
            @Param("address") String address
    );
}