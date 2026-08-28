package com.scanorder.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "daily_revenue_stat")
public class DailyRevenueStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_date", unique = true, nullable = false)
    private LocalDate statDate;

    @Column(name = "stat_year")
    private Integer year;

    @Column(name = "stat_month")
    private Integer month;

    @Column(name = "stat_day")
    private Integer day;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue;

    @Column(name = "effective_orders")
    private Integer effectiveOrders;

    @Column(name = "cancelled_orders")
    private Integer cancelledOrders;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
