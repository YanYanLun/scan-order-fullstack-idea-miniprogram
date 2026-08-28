-- ============================================================
-- 扫码点餐系统 - 微信云托管 / MySQL 8.0+ 建表 DDL
-- 数据库: scan_order
-- ============================================================

CREATE DATABASE IF NOT EXISTS `scan_order` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `scan_order`;

-- 1. 微信用户表 (wx_users) - 支持微信一键登录与会员积分
CREATE TABLE IF NOT EXISTS `wx_users` (
  `id` VARCHAR(64) NOT NULL COMMENT '用户唯一ID (usr_xxx)',
  `open_id` VARCHAR(128) NOT NULL COMMENT '微信小程序 OpenID (微信云托管请求头 x-wx-openid)',
  `union_id` VARCHAR(128) DEFAULT NULL COMMENT '微信开放平台 UnionID',
  `nickname` VARCHAR(64) DEFAULT '微信食客' COMMENT '用户昵称',
  `avatar_url` VARCHAR(512) DEFAULT '' COMMENT '头像URL',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '绑定手机号',
  `member_points` INT DEFAULT 100 COMMENT '会员积分',
  `member_level` VARCHAR(32) DEFAULT '普通会员' COMMENT '会员等级 (普通/黄金/钻石)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `last_login_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_open_id` (`open_id`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信小程序用户表';

-- 2. 菜品分类表 (category)
CREATE TABLE IF NOT EXISTS `category` (
  `id` VARCHAR(64) NOT NULL COMMENT '分类ID',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序权重',
  `is_enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否启用 1-是 0-否',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品分类表';

-- 3. 菜品商品表 (goods)
CREATE TABLE IF NOT EXISTS `goods` (
  `id` VARCHAR(64) NOT NULL COMMENT '商品ID',
  `category_id` VARCHAR(64) NOT NULL COMMENT '关联分类ID',
  `category_name` VARCHAR(64) DEFAULT NULL COMMENT '分类名称',
  `name` VARCHAR(128) NOT NULL COMMENT '菜品名称',
  `price` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '单价',
  `stock` INT NOT NULL DEFAULT 999 COMMENT '库存数量',
  `image` VARCHAR(512) DEFAULT NULL COMMENT '菜品图片',
  `description` TEXT DEFAULT NULL COMMENT '菜品描述',
  `tags` VARCHAR(255) DEFAULT '' COMMENT '标签 (招牌,热销,新品)',
  `is_sold_out` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否估清 1-是 0-否',
  `is_on_off_shelf` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '上下架状态 1-上架 0-下架',
  `sales_count` INT NOT NULL DEFAULT 0 COMMENT '历史销量',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品商品表';

-- 4. 桌台管理表 (dining_table)
CREATE TABLE IF NOT EXISTS `dining_table` (
  `id` VARCHAR(64) NOT NULL COMMENT '桌台ID',
  `table_no` VARCHAR(32) NOT NULL COMMENT '桌号(如A01)',
  `seats` INT NOT NULL DEFAULT 4 COMMENT '容纳人数',
  `qr_code_url` VARCHAR(512) DEFAULT NULL COMMENT '桌台二维码路径',
  `status` VARCHAR(32) NOT NULL DEFAULT 'idle' COMMENT '状态 idle-空闲 occupied-有客',
  `current_order_id` VARCHAR(64) DEFAULT NULL COMMENT '当前关联订单ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_table_no` (`table_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='桌台信息表';

-- 5. 订单主表 (orders) - 关联微信用户与桌号
CREATE TABLE IF NOT EXISTS `orders` (
  `id` VARCHAR(64) NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '系统订单编号 (ORDxxx)',
  `table_no` VARCHAR(32) NOT NULL COMMENT '用餐桌号',
  `dining_type` INT NOT NULL DEFAULT 1 COMMENT '用餐方式 1-堂食 2-外带',
  `total_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `pay_status` INT NOT NULL DEFAULT 0 COMMENT '支付状态 0-未支付 1-已支付 2-已退款',
  `order_status` INT NOT NULL DEFAULT 1 COMMENT '订单状态 1-待接单 2-制作中 3-待出餐 4-已完成 5-已取消',
  `pay_method` VARCHAR(32) DEFAULT 'wechat' COMMENT '支付方式',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '顾客整单备注',
  `cancel_reason` VARCHAR(255) DEFAULT NULL COMMENT '取消/退款原因',
  `open_id` VARCHAR(128) DEFAULT 'wx_openid_demo888' COMMENT '下单微信用户 OpenID',
  `user_id` VARCHAR(64) DEFAULT 'usr_demo888' COMMENT '下单微信用户 ID',
  `user_nickname` VARCHAR(64) DEFAULT '微信食客' COMMENT '下单时用户昵称快照',
  `user_avatar` VARCHAR(512) DEFAULT '' COMMENT '下单时用户头像快照',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `paid_at` DATETIME DEFAULT NULL COMMENT '支付完成时间',
  `completed_at` DATETIME DEFAULT NULL COMMENT '结单完成时间',
  `cancelled_at` DATETIME DEFAULT NULL COMMENT '取消时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_open_id` (`open_id`),
  KEY `idx_table_no` (`table_no`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='扫码点餐主订单表';

-- 6. 订单明细项表 (order_item)
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` VARCHAR(64) NOT NULL COMMENT '明细ID',
  `order_id` VARCHAR(64) NOT NULL COMMENT '关联主订单ID',
  `goods_id` VARCHAR(64) NOT NULL COMMENT '商品ID',
  `goods_name` VARCHAR(128) NOT NULL COMMENT '商品名称',
  `goods_image` VARCHAR(512) DEFAULT NULL COMMENT '商品图片',
  `unit_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '购买单价',
  `item_remark` VARCHAR(255) DEFAULT NULL COMMENT '规格/口味 (如微辣/少冰)',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '购买数量',
  `subtotal` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '小计金额',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单菜品明细表';

-- 7. 门店设置表 (store_info)
CREATE TABLE IF NOT EXISTS `store_info` (
  `id` VARCHAR(64) NOT NULL COMMENT '门店ID',
  `store_name` VARCHAR(128) NOT NULL COMMENT '门店名称',
  `is_business_open` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '营业开关 1-营业 0-打烊',
  `business_hours` VARCHAR(64) DEFAULT '09:00 - 22:00' COMMENT '营业时间',
  `announcement` TEXT DEFAULT NULL COMMENT '店内公告',
  `phone` VARCHAR(32) DEFAULT NULL COMMENT '联系电话',
  `address` VARCHAR(255) DEFAULT NULL COMMENT '门店地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门店基本配置表';

-- 8. 商家管理员表 (merchant_user)
CREATE TABLE IF NOT EXISTS `merchant_user` (
  `id` VARCHAR(64) NOT NULL COMMENT '用户ID',
  `username` VARCHAR(64) NOT NULL COMMENT '登录账号',
  `password` VARCHAR(128) NOT NULL COMMENT '登录密码',
  `role` VARCHAR(32) NOT NULL DEFAULT 'admin' COMMENT '角色 (admin-管理员, staff-店员)',
  `nickname` VARCHAR(64) NOT NULL COMMENT '用户昵称',
  `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像路径',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家系统账号表';

-- 9. 每日营收统计表 (daily_revenue_stat)
CREATE TABLE IF NOT EXISTS `daily_revenue_stat` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `stat_date` DATE NOT NULL UNIQUE COMMENT '统计日期 YYYY-MM-DD',
  `stat_year` INT NOT NULL COMMENT '年份',
  `stat_month` INT NOT NULL COMMENT '月份 1-12',
  `stat_day` INT NOT NULL COMMENT '日期 1-31',
  `total_revenue` DECIMAL(10,2) DEFAULT 0.00 COMMENT '当天实收总额',
  `effective_orders` INT DEFAULT 0 COMMENT '有效完成订单数',
  `cancelled_orders` INT DEFAULT 0 COMMENT '取消订单数',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日营收统计表';
