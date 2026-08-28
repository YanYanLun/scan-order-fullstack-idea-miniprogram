package com.scanorder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "dining_table")
public class DiningTable {

    @Id
    private String id;
    private String tableNo;
    private Integer seats;
    private String qrCodeUrl;
    private String status;
    private String currentOrderId;
}
