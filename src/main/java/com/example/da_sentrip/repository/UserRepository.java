package com.example.da_sentrip.repository;

import com.example.da_sentrip.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGmail(String gmail);
}
