package com.scanorder.repository;

import com.scanorder.entity.MerchantUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantUserRepository extends JpaRepository<MerchantUser, String> {
    Optional<MerchantUser> findByUsernameAndPassword(String username, String password);
}
