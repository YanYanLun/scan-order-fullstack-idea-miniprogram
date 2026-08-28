package com.scanorder.controller;

import com.scanorder.common.Result;
import com.scanorder.context.WxCloudContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信云托管探针健康检测与云框架环境探测
 */
@RestController
@RequestMapping
public class HealthController {

    @GetMapping({"/health", "/api/health"})
    public Result<Map<String, Object>> health() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "UP");
        map.put("framework", "WeChat CloudRun + SpringBoot 3");
        map.put("timestamp", System.currentTimeMillis());
        return Result.success("服务正常运行中", map);
    }

    @GetMapping({"/wx/cloud/info", "/api/wx/cloud/info"})
    public Result<Map<String, Object>> getWxCloudInfo() {
        WxCloudContext ctx = WxCloudContext.get();
        Map<String, Object> map = new HashMap<>();
        map.put("openId", ctx.getOpenId());
        map.put("unionId", ctx.getUnionId());
        map.put("source", ctx.getSource());
        map.put("env", ctx.getEnv());
        map.put("isWxCloudHosting", ctx.getOpenId() != null);
        return Result.success(map);
    }
}
