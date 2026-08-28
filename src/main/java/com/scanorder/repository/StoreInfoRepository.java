package com.scanorder.repository;

import com.scanorder.entity.StoreInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreInfoRepository extends JpaRepository<StoreInfo, String> {
}
