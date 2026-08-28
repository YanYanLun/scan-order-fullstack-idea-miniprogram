package com.scanorder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "category")
public class Category {

    @Id
    private String id;
    private String name;
    private Integer sortOrder;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
}
