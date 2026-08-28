package com.scanorder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "store_info")
public class StoreInfo {

    @Id
    private String id;
    private String storeName;
    private Boolean isBusinessOpen;
    private String businessHours;
    private String announcement;
    private String phone;
    private String address;
}
