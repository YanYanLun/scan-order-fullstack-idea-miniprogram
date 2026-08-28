package com.scanorder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "merchant_user")
public class MerchantUser {

    @Id
    private String id;
    private String username;
    private String password;
    private String role;
    private String nickname;
    private String avatar;
}
