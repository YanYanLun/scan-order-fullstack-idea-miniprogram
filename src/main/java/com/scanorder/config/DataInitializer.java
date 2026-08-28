package com.scanorder.config;

import com.scanorder.entity.*;
import com.scanorder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 数据库预置数据初始化器 (在 H2/MySQL 启动后自动注入全套测试数据与初始管理员账号)
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private DiningTableRepository diningTableRepository;

    @Autowired
    private StoreInfoRepository storeInfoRepository;

    @Autowired
    private MerchantUserRepository merchantUserRepository;

    @Autowired
    private WxUserRepository wxUserRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private DailyRevenueStatRepository dailyRevenueStatRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }

        System.out.println("📦 正在自动注入扫码点餐系统初始数据 (分类、菜品、桌台、门店与管理员账号)...");

        // 1. 初始化分类
        Category c1 = new Category(); c1.setId("c1"); c1.setName("🔥 热销推荐"); c1.setSortOrder(1); c1.setIsEnabled(true); c1.setCreatedAt(LocalDateTime.now());
        Category c2 = new Category(); c2.setId("c2"); c2.setName("🍜 主食精选"); c2.setSortOrder(2); c2.setIsEnabled(true); c2.setCreatedAt(LocalDateTime.now());
        Category c3 = new Category(); c3.setId("c3"); c3.setName("🥤 招牌饮品"); c3.setSortOrder(3); c3.setIsEnabled(true); c3.setCreatedAt(LocalDateTime.now());
        Category c4 = new Category(); c4.setId("c4"); c4.setName("🍢 特色小吃"); c4.setSortOrder(4); c4.setIsEnabled(true); c4.setCreatedAt(LocalDateTime.now());
        Category c5 = new Category(); c5.setId("c5"); c5.setName("🍰 精致甜品"); c5.setSortOrder(5); c5.setIsEnabled(true); c5.setCreatedAt(LocalDateTime.now());
        categoryRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));

        // 2. 初始化商品菜品
        Goods g1 = new Goods(); g1.setId("g1"); g1.setCategoryId("c1"); g1.setCategoryName("🔥 热销推荐"); g1.setName("云居招牌红烧牛肉面"); g1.setPrice(new BigDecimal("32.00")); g1.setStock(200); g1.setImage("https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=600&q=80"); g1.setDescription("炖煮6小时高汤，搭配精选牛腩与爽滑手工拉面"); g1.setTags("招牌,热销"); g1.setIsSoldOut(false); g1.setIsOnOffShelf(true); g1.setSalesCount(890); g1.setCreatedAt(LocalDateTime.now());
        Goods g2 = new Goods(); g2.setId("g2"); g2.setCategoryId("c1"); g2.setCategoryName("🔥 热销推荐"); g2.setName("秘制黑椒西牛排套餐"); g2.setPrice(new BigDecimal("68.00")); g2.setStock(100); g2.setImage("https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=600&q=80"); g2.setDescription("精选优质谷饲牛排，鲜嫩多汁，含薯条与蔬菜沙拉"); g2.setTags("热销"); g2.setIsSoldOut(false); g2.setIsOnOffShelf(true); g2.setSalesCount(450); g2.setCreatedAt(LocalDateTime.now());
        Goods g3 = new Goods(); g3.setId("g3"); g3.setCategoryId("c2"); g3.setCategoryName("🍜 主食精选"); g3.setName("传统手工鲜肉小笼包(6只)"); g3.setPrice(new BigDecimal("24.00")); g3.setStock(300); g3.setImage("https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=600&q=80"); g3.setDescription("现点现蒸，皮薄馅大，满口汤汁浓郁"); g3.setTags("新品"); g3.setIsSoldOut(false); g3.setIsOnOffShelf(true); g3.setSalesCount(620); g3.setCreatedAt(LocalDateTime.now());
        Goods g4 = new Goods(); g4.setId("g4"); g4.setCategoryId("c3"); g4.setCategoryName("🥤 招牌饮品"); g4.setName("冰镇港式杨枝甘露"); g4.setPrice(new BigDecimal("18.00")); g4.setStock(500); g4.setImage("https://images.unsplash.com/photo-1551024709-8f23befc6f87?auto=format&fit=crop&w=600&q=80"); g4.setDescription("新鲜芒果肉、西柚粒与西米露，奶香甘甜清爽"); g4.setTags("热销"); g4.setIsSoldOut(false); g4.setIsOnOffShelf(true); g4.setSalesCount(1200); g4.setCreatedAt(LocalDateTime.now());
        Goods g5 = new Goods(); g5.setId("g5"); g5.setCategoryId("c3"); g5.setCategoryName("🥤 招牌饮品"); g5.setName("鲜榨四季春芝士奶盖"); g5.setPrice(new BigDecimal("16.00")); g5.setStock(400); g5.setImage("https://images.unsplash.com/photo-1517256064527-09c73fc73e38?auto=format&fit=crop&w=600&q=80"); g5.setDescription("高山四季春茶汤，淋上特调咸香浓郁芝士奶盖"); g5.setTags(""); g5.setIsSoldOut(false); g5.setIsOnOffShelf(true); g5.setSalesCount(980); g5.setCreatedAt(LocalDateTime.now());
        Goods g6 = new Goods(); g6.setId("g6"); g6.setCategoryId("c4"); g6.setCategoryName("🍢 特色小吃"); g6.setName("黄金香酥大鸡翅(2只)"); g6.setPrice(new BigDecimal("16.00")); g6.setStock(250); g6.setImage("https://images.unsplash.com/photo-1527477396000-e27163b481c2?auto=format&fit=crop&w=600&q=80"); g6.setDescription("外酥里嫩，秘制香料腌制，香辣可口"); g6.setTags("热销"); g6.setIsSoldOut(false); g6.setIsOnOffShelf(true); g6.setSalesCount(710); g6.setCreatedAt(LocalDateTime.now());
        Goods g7 = new Goods(); g7.setId("g7"); g7.setCategoryId("c5"); g7.setCategoryName("🍰 精致甜品"); g7.setName("宇治抹茶日式慕斯"); g7.setPrice(new BigDecimal("22.00")); g7.setStock(80); g7.setImage("https://images.unsplash.com/photo-1533134242443-d4fd215305ad?auto=format&fit=crop&w=600&q=80"); g7.setDescription("进口宇治抹茶粉制作，回甘悠长，甜而不腻"); g7.setTags("新品"); g7.setIsSoldOut(false); g7.setIsOnOffShelf(true); g7.setSalesCount(310); g7.setCreatedAt(LocalDateTime.now());
        goodsRepository.saveAll(Arrays.asList(g1, g2, g3, g4, g5, g6, g7));

        // 3. 初始化桌台
        DiningTable t1 = new DiningTable(); t1.setId("t1"); t1.setTableNo("A01"); t1.setSeats(4); t1.setQrCodeUrl("/qr/A01.png"); t1.setStatus("occupied"); t1.setCurrentOrderId("o1");
        DiningTable t2 = new DiningTable(); t2.setId("t2"); t2.setTableNo("A02"); t2.setSeats(2); t2.setQrCodeUrl("/qr/A02.png"); t2.setStatus("idle");
        DiningTable t3 = new DiningTable(); t3.setId("t3"); t3.setTableNo("A03"); t3.setSeats(4); t3.setQrCodeUrl("/qr/A03.png"); t3.setStatus("idle");
        DiningTable t4 = new DiningTable(); t4.setId("t4"); t4.setTableNo("B01"); t4.setSeats(6); t4.setQrCodeUrl("/qr/B01.png"); t4.setStatus("idle");
        DiningTable t5 = new DiningTable(); t5.setId("t5"); t5.setTableNo("B02"); t5.setSeats(8); t5.setQrCodeUrl("/qr/B02.png"); t5.setStatus("idle");
        DiningTable t6 = new DiningTable(); t6.setId("t6"); t6.setTableNo("VIP01"); t6.setSeats(12); t6.setQrCodeUrl("/qr/VIP01.png"); t6.setStatus("idle");
        diningTableRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5, t6));

        // 4. 初始化门店信息
        StoreInfo store = new StoreInfo(); store.setId("s1"); store.setStoreName("寻味云居 · 扫码点餐旗舰店"); store.setIsBusinessOpen(true); store.setBusinessHours("09:00 - 22:00"); store.setAnnouncement("欢迎光临寻味云居！本店食材每日新鲜直采，扫码即可秒速下单。"); store.setPhone("021-88888888"); store.setAddress("上海市浦东新区世纪大道88号");
        storeInfoRepository.save(store);

        // 5. 初始化商家管理员账号
        MerchantUser admin = new MerchantUser(); admin.setId("u1"); admin.setUsername("admin"); admin.setPassword("admin123"); admin.setRole("admin"); admin.setNickname("总店店长"); admin.setAvatar("https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80");
        MerchantUser staff = new MerchantUser(); staff.setId("u2"); staff.setUsername("staff"); staff.setPassword("staff123"); staff.setRole("staff"); staff.setNickname("前台值班员"); staff.setAvatar("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80");
        merchantUserRepository.saveAll(Arrays.asList(admin, staff));

        // 6. 初始化微信演示用户
        WxUser demoUser = new WxUser(); demoUser.setId("usr_demo888"); demoUser.setOpenId("wx_openid_demo888"); demoUser.setNickname("寻味吃货小明"); demoUser.setAvatarUrl("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=120"); demoUser.setPhone("13888888888"); demoUser.setMemberPoints(350); demoUser.setMemberLevel("黄金会员"); demoUser.setCreatedAt(LocalDateTime.now()); demoUser.setLastLoginAt(LocalDateTime.now());
        wxUserRepository.save(demoUser);

        // 7. 初始化初始订单
        Order o1 = new Order(); o1.setId("o1"); o1.setOrderNo("ORD20260801001"); o1.setTableNo("A01"); o1.setDiningType(1); o1.setTotalAmount(new BigDecimal("56.00")); o1.setPayStatus(1); o1.setOrderStatus(1); o1.setPayMethod("wechat"); o1.setRemark("牛肉面微辣，多放香菜"); o1.setOpenId("wx_openid_demo888"); o1.setUserId("usr_demo888"); o1.setUserNickname("寻味吃货小明"); o1.setUserAvatar("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=120"); o1.setCreatedAt(LocalDateTime.now()); o1.setPaidAt(LocalDateTime.now());
        orderRepository.save(o1);

        OrderItem oi1 = new OrderItem(); oi1.setId("oi1"); oi1.setOrderId("o1"); oi1.setGoodsId("g1"); oi1.setGoodsName("云居招牌红烧牛肉面"); oi1.setGoodsImage("https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=600&q=80"); oi1.setUnitPrice(new BigDecimal("32.00")); oi1.setItemRemark("微辣"); oi1.setQuantity(1); oi1.setSubtotal(new BigDecimal("32.00"));
        OrderItem oi2 = new OrderItem(); oi2.setId("oi2"); oi2.setOrderId("o1"); oi2.setGoodsId("g3"); oi2.setGoodsName("传统手工鲜肉小笼包(6只)"); oi2.setGoodsImage("https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=600&q=80"); oi2.setUnitPrice(new BigDecimal("24.00")); oi2.setItemRemark(""); oi2.setQuantity(1); oi2.setSubtotal(new BigDecimal("24.00"));
        orderItemRepository.saveAll(Arrays.asList(oi1, oi2));

        // 8. 每日营收初始统计
        DailyRevenueStat stat = new DailyRevenueStat(); stat.setStatDate(LocalDate.now()); stat.setYear(LocalDate.now().getYear()); stat.setMonth(LocalDate.now().getMonthValue()); stat.setDay(LocalDate.now().getDayOfMonth()); stat.setTotalRevenue(new BigDecimal("3688.50")); stat.setEffectiveOrders(48); stat.setCancelledOrders(2); stat.setCreatedAt(LocalDateTime.now());
        dailyRevenueStatRepository.save(stat);

        System.out.println("✅ 扫码点餐初始数据与管理员账号已注入成功！");
    }
}
