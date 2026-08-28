package com.scanorder.repository;

import com.scanorder.entity.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WxUserRepository extends JpaRepository<WxUser, String> {
    Optional<WxUser> findByOpenId(String openId);
}
