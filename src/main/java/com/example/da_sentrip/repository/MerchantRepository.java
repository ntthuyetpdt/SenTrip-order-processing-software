package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("""
        SELECT m FROM Merchant m
        WHERE (:merchantName IS NULL OR m.merchantName LIKE CONCAT('%', :merchantName, '%'))
          AND (:address IS NULL OR m.address LIKE CONCAT('%', :address, '%'))
          AND (:businessLicense IS NULL OR m.businessLicense LIKE CONCAT('%', :businessLicense, '%'))
    """)
    List<Merchant> findBySearch(
            @Param("merchantName") String merchantName,
            @Param("address") String address,
            @Param("businessLicense") String businessLicense
    );
}
