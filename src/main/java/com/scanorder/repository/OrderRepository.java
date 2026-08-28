package com.scanorder.repository;

import com.scanorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByOrderStatus(Integer orderStatus);
    List<Order> findByOpenIdOrderByCreatedAtDesc(String openId);
    List<Order> findByTableNoOrderByCreatedAtDesc(String tableNo);
    List<Order> findAllByOrderByCreatedAtDesc();
}
