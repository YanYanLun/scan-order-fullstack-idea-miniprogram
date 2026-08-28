package com.scanorder.repository;

import com.scanorder.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, String> {
    List<Goods> findByIsOnOffShelfTrue();
    List<Goods> findByCategoryId(String categoryId);
}
