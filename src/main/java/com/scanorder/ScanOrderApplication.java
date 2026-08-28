package com.scanorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScanOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScanOrderApplication.class, args);
        System.out.println("========================================================================");
        System.out.println("🚀 扫码点餐微信云框架 (WeChat CloudRun + SpringBoot) 服务已成功启动！");
        System.out.println("📍 本地接口测试地址: http://localhost:8080/api");
        System.out.println("📍 微信云托管健康检查: http://localhost:8080/api/health");
        System.out.println("========================================================================");
    }

}
