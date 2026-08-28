package com.scanorder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    private String id;
    private String orderId;
    private String goodsId;
    private String goodsName;
    private String goodsImage;
    private BigDecimal unitPrice;
    private String itemRemark;
    private Integer quantity;
    private BigDecimal subtotal;
}
