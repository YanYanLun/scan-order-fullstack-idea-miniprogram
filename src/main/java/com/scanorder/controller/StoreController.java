package com.scanorder.controller;

import com.scanorder.common.Result;
import com.scanorder.entity.StoreInfo;
import com.scanorder.repository.StoreInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/store", "/api/store"})
public class StoreController {

    @Autowired
    private StoreInfoRepository storeInfoRepository;

    @GetMapping({"", "/info"})
    public Result<StoreInfo> getStoreInfo() {
        return storeInfoRepository.findAll().stream().findFirst()
                .map(Result::success)
                .orElseGet(() -> {
                    StoreInfo info = new StoreInfo();
                    info.setId("s1");
                    info.setStoreName("寻味云居 · 扫码点餐旗舰店");
                    info.setIsBusinessOpen(true);
                    info.setBusinessHours("09:00 - 22:00");
                    info.setAnnouncement("欢迎光临寻味云居！本店食材每日新鲜直采。");
                    info.setPhone("021-88888888");
                    info.setAddress("上海市浦东新区世纪大道88号");
                    return Result.success(info);
                });
    }

    @PostMapping("/status")
    public Result<StoreInfo> toggleBusinessStatus(@RequestBody Map<String, Object> body) {
        StoreInfo info = storeInfoRepository.findAll().stream().findFirst().orElseGet(() -> {
            StoreInfo s = new StoreInfo();
            s.setId("s1");
            s.setStoreName("寻味云居 · 扫码点餐旗舰店");
            return s;
        });

        if (body.containsKey("isBusinessOpen")) {
            info.setIsBusinessOpen((Boolean) body.get("isBusinessOpen"));
        }
        storeInfoRepository.save(info);
        return Result.success("营业状态已更新", info);
    }
}
