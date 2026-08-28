-- ============================================================
-- 扫码点餐系统 - 初始演示数据注入 DML
-- ============================================================
USE `scan_order`;

-- 1. 预置微信演示用户
INSERT IGNORE INTO `wx_users` (`id`, `open_id`, `nickname`, `avatar_url`, `phone`, `member_points`, `member_level`) VALUES
('usr_demo888', 'wx_openid_demo888', '寻味吃货小明', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=120', '13888888888', 350, '黄金会员');

-- 2. 预置菜品分类
INSERT IGNORE INTO `category` (`id`, `name`, `sort_order`, `is_enabled`) VALUES
('c1', '🔥 热销推荐', 1, 1),
('c2', '🍜 主食精选', 2, 1),
('c3', '🥤 招牌饮品', 3, 1),
('c4', '🍢 特色小吃', 4, 1),
('c5', '🍰 精致甜品', 5, 1);

-- 3. 预置菜品商品
INSERT IGNORE INTO `goods` (`id`, `category_id`, `category_name`, `name`, `price`, `stock`, `image`, `description`, `tags`, `is_sold_out`, `is_on_off_shelf`, `sales_count`) VALUES
('g1', 'c1', '🔥 热销推荐', '云居招牌红烧牛肉面', 32.00, 200, 'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=600&q=80', '炖煮6小时高汤，搭配精选牛腩与爽滑手工拉面', '招牌,热销', 0, 1, 890),
('g2', 'c1', '🔥 热销推荐', '秘制黑椒西牛排套餐', 68.00, 100, 'https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=600&q=80', '选原切优质谷饲牛排，鲜嫩多汁，含薯条与蔬菜沙拉', '热销', 0, 1, 450),
('g3', 'c2', '🍜 主食精选', '传统手工鲜肉小笼包(6只)', 24.00, 300, 'https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=600&q=80', '现点现蒸，皮薄馅大，满口汤汁浓郁', '新品', 0, 1, 620),
('g4', 'c3', '🥤 招牌饮品', '冰镇港式杨枝甘露', 18.00, 500, 'https://images.unsplash.com/photo-1551024709-8f23befc6f87?auto=format&fit=crop&w=600&q=80', '新鲜芒果肉、西柚粒与西米露，奶香甘甜清爽', '热销', 0, 1, 1200),
('g5', 'c3', '🥤 招牌饮品', '鲜榨四季春芝士奶盖', 16.00, 400, 'https://images.unsplash.com/photo-1517256064527-09c73fc73e38?auto=format&fit=crop&w=600&q=80', '高山四季春茶汤，淋上特调咸香浓郁芝士奶盖', '', 0, 1, 980),
('g6', 'c4', '🍢 特色小吃', '黄金香酥大鸡翅(2只)', 16.00, 250, 'https://images.unsplash.com/photo-1527477396000-e27163b481c2?auto=format&fit=crop&w=600&q=80', '外酥里嫩，秘制香料腌制，香辣可口', '热销', 0, 1, 710),
('g7', 'c5', '🍰 精致甜品', '宇治抹茶日式慕斯', 22.00, 80, 'https://images.unsplash.com/photo-1533134242443-d4fd215305ad?auto=format&fit=crop&w=600&q=80', '进口宇治抹茶粉制作，回甘悠长，甜而不腻', '新品', 0, 1, 310);

-- 4. 预置桌台
INSERT IGNORE INTO `dining_table` (`id`, `table_no`, `seats`, `qr_code_url`, `status`, `current_order_id`) VALUES
('t1', 'A01', 4, '/qr/A01.png', 'occupied', 'o1'),
('t2', 'A02', 2, '/qr/A02.png', 'idle', NULL),
('t3', 'A03', 4, '/qr/A03.png', 'idle', NULL),
('t4', 'B01', 6, '/qr/B01.png', 'idle', NULL),
('t5', 'B02', 8, '/qr/B02.png', 'idle', NULL),
('t6', 'VIP01', 12, '/qr/VIP01.png', 'idle', NULL);

-- 5. 预置门店信息
INSERT IGNORE INTO `store_info` (`id`, `store_name`, `is_business_open`, `business_hours`, `announcement`, `phone`, `address`) VALUES
('s1', '寻味云居 · 扫码点餐旗舰店', 1, '09:00 - 22:00', '欢迎光临寻味云居！本店食材每日新鲜直采，扫码即可秒速下单。', '021-88888888', '上海市浦东新区世纪大道88号');

-- 6. 预置商家账号 (账号密码: admin / admin123, staff / staff123)
INSERT IGNORE INTO `merchant_user` (`id`, `username`, `password`, `role`, `nickname`, `avatar`) VALUES
('u1', 'admin', 'admin123', 'admin', '总店店长', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80'),
('u2', 'staff', 'staff123', 'staff', '前台值班员', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80');

-- 7. 预置初始演示订单
INSERT IGNORE INTO `orders` (`id`, `order_no`, `table_no`, `dining_type`, `total_amount`, `pay_status`, `order_status`, `remark`, `open_id`, `user_id`, `user_nickname`, `user_avatar`, `created_at`, `paid_at`) VALUES
('o1', 'ORD20260801001', 'A01', 1, 56.00, 1, 1, '牛肉面微辣，多放香菜', 'wx_openid_demo888', 'usr_demo888', '寻味吃货小明', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=120', NOW(), NOW()),
('o2', 'ORD20260801002', 'A02', 2, 34.00, 1, 4, '外带打包', 'wx_openid_demo888', 'usr_demo888', '寻味吃货小明', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=120', NOW(), NOW());

INSERT IGNORE INTO `order_item` (`id`, `order_id`, `goods_id`, `goods_name`, `goods_image`, `unit_price`, `item_remark`, `quantity`, `subtotal`) VALUES
('oi1', 'o1', 'g1', '云居招牌红烧牛肉面', 'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=600&q=80', 32.00, '微辣', 1, 32.00),
('oi2', 'o1', 'g3', '传统手工鲜肉小笼包(6只)', 'https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=600&q=80', 24.00, '', 1, 24.00),
('oi3', 'o2', 'g4', '冰镇港式杨枝甘露', 'https://images.unsplash.com/photo-1551024709-8f23befc6f87?auto=format&fit=crop&w=600&q=80', 18.00, '正常甜', 1, 18.00),
('oi4', 'o2', 'g5', '鲜榨四季春芝士奶盖', 'https://images.unsplash.com/photo-1517256064527-09c73fc73e38?auto=format&fit=crop&w=600&q=80', 16.00, '少冰', 1, 16.00);
