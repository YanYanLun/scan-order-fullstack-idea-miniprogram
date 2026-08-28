package com.scanorder.repository;

import com.scanorder.entity.DailyRevenueStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyRevenueStatRepository extends JpaRepository<DailyRevenueStat, Long> {
    Optional<DailyRevenueStat> findByStatDate(LocalDate statDate);
    List<DailyRevenueStat> findByYear(Integer year);
    List<DailyRevenueStat> findByYearAndMonth(Integer year, Integer month);
}
