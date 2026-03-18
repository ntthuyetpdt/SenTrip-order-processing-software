package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.Merchant;
import com.example.da_sentrip.model.entity.PayoutRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PayoutRequestRepository extends JpaRepository<PayoutRequest, Long> {
    boolean existsByOrderCode(String orderCode);
    List<PayoutRequest> findByMerchant(Merchant merchant);
}