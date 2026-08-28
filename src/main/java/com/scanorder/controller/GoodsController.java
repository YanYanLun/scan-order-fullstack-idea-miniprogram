package com.scanorder.controller;

import com.scanorder.common.Result;
import com.scanorder.entity.Goods;
import com.scanorder.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping({"/goods", "/api/goods"})
public class GoodsController {

    @Autowired
    private GoodsRepository goodsRepository;

    @GetMapping
    public Result<List<Goods>> getGoodsList(@RequestParam(name = "categoryId", required = false) String categoryId) {
        if (categoryId != null && !categoryId.trim().isEmpty() && !"all".equalsIgnoreCase(categoryId)) {
            return Result.success(goodsRepository.findByCategoryId(categoryId));
        }
        return Result.success(goodsRepository.findAll());
    }

    @GetMapping("/{id}")
    public Result<Goods> getGoodsDetail(@PathVariable(name = "id") String id) {
        return goodsRepository.findById(id)
                .map(Result::success)
                .orElse(Result.error("菜品不存在"));
    }

    @PostMapping
    public Result<Goods> createGoods(@RequestBody Goods goods) {
        if (goods.getId() == null || goods.getId().isEmpty()) {
            goods.setId("g_" + UUID.randomUUID().toString().substring(0, 8));
        }
        if (goods.getCreatedAt() == null) {
            goods.setCreatedAt(LocalDateTime.now());
        }
        if (goods.getIsSoldOut() == null) goods.setIsSoldOut(false);
        if (goods.getIsOnOffShelf() == null) goods.setIsOnOffShelf(true);
        if (goods.getSalesCount() == null) goods.setSalesCount(0);
        goodsRepository.save(goods);
        return Result.success("菜品创建成功", goods);
    }

    @PutMapping("/{id}")
    public Result<Goods> updateGoods(@PathVariable(name = "id") String id, @RequestBody Map<String, Object> body) {
        return goodsRepository.findById(id).map(goods -> {
            if (body.containsKey("isSoldOut")) {
                goods.setIsSoldOut((Boolean) body.get("isSoldOut"));
            }
            if (body.containsKey("isOnOffShelf")) {
                goods.setIsOnOffShelf((Boolean) body.get("isOnOffShelf"));
            }
            if (body.containsKey("name")) {
                goods.setName((String) body.get("name"));
            }
            if (body.containsKey("price")) {
                goods.setPrice(new java.math.BigDecimal(body.get("price").toString()));
            }
            if (body.containsKey("stock")) {
                goods.setStock(((Number) body.get("stock")).intValue());
            }
            if (body.containsKey("description")) {
                goods.setDescription((String) body.get("description"));
            }
            if (body.containsKey("image")) {
                goods.setImage((String) body.get("image"));
            }
            goodsRepository.save(goods);
            return Result.success(goods);
        }).orElse(Result.error("菜品不存在"));
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteGoods(@PathVariable(name = "id") String id) {
        if (goodsRepository.existsById(id)) {
            goodsRepository.deleteById(id);
            return Result.success("删除成功", id);
        }
        return Result.error("菜品不存在");
    }
}
