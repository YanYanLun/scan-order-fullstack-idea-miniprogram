package com.scanorder.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String id;
    private String orderNo;
    private String tableNo;
    private Integer diningType; // 1堂食, 2外带
    private BigDecimal totalAmount;
    private Integer payStatus; // 0未支付, 1已支付, 2已退款
    private Integer orderStatus; // 1待接单, 2制作中, 3待出餐, 4已完成, 5已取消
    private String payMethod; // wechat
    private String remark;
    private String cancelReason;

    // 关联微信用户信息
    private String openId;
    private String userId;
    private String userNickname;
    private String userAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime paidAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime completedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime cancelledAt;

    @Transient
    private List<OrderItem> items = new ArrayList<>();
}
