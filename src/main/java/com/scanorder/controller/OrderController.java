package com.scanorder.controller;

import com.scanorder.common.Result;
import com.scanorder.context.WxCloudContext;
import com.scanorder.entity.Order;
import com.scanorder.entity.OrderItem;
import com.scanorder.entity.WxUser;
import com.scanorder.repository.DiningTableRepository;
import com.scanorder.repository.OrderItemRepository;
import com.scanorder.repository.OrderRepository;
import com.scanorder.repository.WxUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 扫码点餐订单核心控制器 (支持微信云托管 OpenID 自动关联、全流程状态机流转)
 */
@RestController
@RequestMapping({"/orders", "/api/orders"})
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private DiningTableRepository diningTableRepository;

    @Autowired
    private WxUserRepository wxUserRepository;

    @GetMapping
    public Result<List<Order>> getOrders(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "tableNo", required = false) String tableNo,
            @RequestParam(name = "openId", required = false) String openId) {
        try {
            List<Order> orders;
            
            // 1. 如果传了特定 openId，按用户查询历史订单
            if (openId != null && !openId.trim().isEmpty()) {
                orders = orderRepository.findByOpenIdOrderByCreatedAtDesc(openId);
            } else if (tableNo != null && !tableNo.trim().isEmpty()) {
                orders = orderRepository.findByTableNoOrderByCreatedAtDesc(tableNo);
            } else if (status != null && !status.trim().isEmpty() && !"all".equalsIgnoreCase(status) && !"0".equals(status)) {
                try {
                    Integer statusCode = Integer.parseInt(status);
                    orders = orderRepository.findByOrderStatus(statusCode);
                } catch (NumberFormatException e) {
                    orders = orderRepository.findAllByOrderByCreatedAtDesc();
                }
            } else {
                orders = orderRepository.findAllByOrderByCreatedAtDesc();
            }

            if (orders == null) {
                orders = new java.util.ArrayList<>();
            }

            for (Order o : orders) {
                if (o != null && o.getId() != null) {
                    List<OrderItem> items = orderItemRepository.findByOrderId(o.getId());
                    o.setItems(items != null ? items : new java.util.ArrayList<>());
                }
            }

            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(500, "获取订单列表异常: " + (e.getMessage() != null ? e.getMessage() : "数据库连接失败"));
        }
    }

    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable(name = "id") String id) {
        return orderRepository.findById(id).map(order -> {
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            order.setItems(items != null ? items : new java.util.ArrayList<>());
            return Result.success(order);
        }).orElse(Result.error("订单不存在"));
    }

    @PostMapping
    public Result<Order> createOrder(@RequestBody Map<String, Object> body) {
        String id = "ord_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        Order order = new Order();
        order.setId(id);
        order.setOrderNo("ORD" + System.currentTimeMillis());
        
        String tableNo = (String) body.getOrDefault("tableNo", "A01");
        order.setTableNo(tableNo);
        order.setDiningType(((Number) body.getOrDefault("diningType", 1)).intValue());
        order.setTotalAmount(new BigDecimal(body.getOrDefault("totalAmount", "0.00").toString()));
        order.setPayStatus(1); // 模拟微信支付成功 (1-已支付)
        order.setOrderStatus(1); // 1-待接单
        order.setPayMethod("wechat");
        order.setRemark((String) body.get("remark"));
        
        // 获取微信用户身份 (优先取微信云托管 Header)
        String openId = (String) body.get("openId");
        if (openId == null || openId.trim().isEmpty()) {
            openId = WxCloudContext.get().getOpenId();
        }
        if (openId == null || openId.trim().isEmpty()) {
            openId = "wx_openid_demo888";
        }
        order.setOpenId(openId);
        order.setUserId((String) body.getOrDefault("userId", "usr_demo888"));
        order.setUserNickname((String) body.getOrDefault("userNickname", "寻味吃货小明"));
        order.setUserAvatar((String) body.getOrDefault("userAvatar", "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=120"));

        order.setCreatedAt(LocalDateTime.now());
        order.setPaidAt(LocalDateTime.now());

        orderRepository.save(order);

        // 联动更新桌台状态为 occupied
        diningTableRepository.findByTableNo(tableNo).ifPresent(t -> {
            t.setStatus("occupied");
            t.setCurrentOrderId(id);
            diningTableRepository.save(t);
        });

        // 保存订单菜品明细
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        if (items != null) {
            for (Map<String, Object> itemMap : items) {
                OrderItem item = new OrderItem();
                item.setId("oi_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
                item.setOrderId(id);
                item.setGoodsId((String) itemMap.get("goodsId"));
                item.setGoodsName((String) itemMap.get("goodsName"));
                item.setGoodsImage((String) itemMap.get("goodsImage"));
                item.setUnitPrice(new BigDecimal(itemMap.getOrDefault("unitPrice", "0.00").toString()));
                item.setItemRemark((String) itemMap.getOrDefault("itemRemark", ""));
                item.setQuantity(((Number) itemMap.getOrDefault("quantity", 1)).intValue());
                item.setSubtotal(new BigDecimal(itemMap.getOrDefault("subtotal", "0.00").toString()));
                orderItemRepository.save(item);
            }
        }

        // 累计微信用户会员积分 (1元消费 = 1积分)
        wxUserRepository.findByOpenId(openId).ifPresent(u -> {
            int extraPoints = order.getTotalAmount().intValue();
            u.setMemberPoints((u.getMemberPoints() != null ? u.getMemberPoints() : 0) + extraPoints);
            wxUserRepository.save(u);
        });

        return Result.success("下单成功", order);
    }

    @PostMapping("/{id}/pay")
    public Result<Order> payOrder(@PathVariable(name = "id") String id) {
        return orderRepository.findById(id).map(order -> {
            order.setPayStatus(1);
            if (order.getOrderStatus() == 5) {
                order.setOrderStatus(1);
            }
            order.setPaidAt(LocalDateTime.now());
            orderRepository.save(order);
            return Result.success("支付成功", order);
        }).orElse(Result.error("订单不存在"));
    }

    @PostMapping("/{id}/status")
    public Result<Order> updateOrderStatus(@PathVariable(name = "id") String id, @RequestBody Map<String, Object> body) {
        return orderRepository.findById(id).map(order -> {
            if (body.containsKey("status")) {
                int status = ((Number) body.get("status")).intValue();
                order.setOrderStatus(status);
                if (status == 4) {
                    order.setCompletedAt(LocalDateTime.now());
                    // 结单后释放桌台
                    diningTableRepository.findByTableNo(order.getTableNo()).ifPresent(t -> {
                        t.setStatus("idle");
                        t.setCurrentOrderId(null);
                        diningTableRepository.save(t);
                    });
                } else if (status == 5) {
                    order.setCancelledAt(LocalDateTime.now());
                    if (body.containsKey("cancelReason")) {
                        order.setCancelReason((String) body.get("cancelReason"));
                    }
                }
            }
            orderRepository.save(order);
            return Result.success("订单状态更新成功", order);
        }).orElse(Result.error("订单不存在"));
    }

    @PostMapping("/{id}/cancel")
    public Result<Order> cancelOrder(@PathVariable(name = "id") String id, @RequestBody(required = false) Map<String, String> body) {
        return orderRepository.findById(id).map(order -> {
            order.setOrderStatus(5); // 5-已取消
            order.setCancelledAt(LocalDateTime.now());
            if (body != null && body.containsKey("reason")) {
                order.setCancelReason(body.get("reason"));
            }
            orderRepository.save(order);

            // 释放桌台
            diningTableRepository.findByTableNo(order.getTableNo()).ifPresent(t -> {
                if (id.equals(t.getCurrentOrderId())) {
                    t.setStatus("idle");
                    t.setCurrentOrderId(null);
                    diningTableRepository.save(t);
                }
            });

            return Result.success("订单已取消", order);
        }).orElse(Result.error("订单不存在"));
    }
}
