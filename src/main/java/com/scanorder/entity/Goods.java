package com.scanorder.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "goods")
public class Goods {

    @Id
    private String id;
    private String categoryId;
    private String categoryName;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private String description;
    private String tags;
    private Boolean isSoldOut;
    private Boolean isOnOffShelf;
    private Integer salesCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
}
