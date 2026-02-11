package com.example.da_sentrip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.da_sentrip.model.entity.DataSource;
import java.util.Optional;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {
    boolean existsByImageUrl(String imageUrl);
    void deleteByImageUrl(String imageUrl);
    Optional<DataSource> findByImageUrlContaining(String fileName);
    Optional<DataSource> findTopByOrderByIdDesc();

}
