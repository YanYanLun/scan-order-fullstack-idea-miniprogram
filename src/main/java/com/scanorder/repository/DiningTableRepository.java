package com.scanorder.repository;

import com.scanorder.entity.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiningTableRepository extends JpaRepository<DiningTable, String> {
    Optional<DiningTable> findByTableNo(String tableNo);
}
