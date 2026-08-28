package com.scanorder.controller;

import com.scanorder.common.Result;
import com.scanorder.entity.MerchantUser;
import com.scanorder.repository.MerchantUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/merchant", "/api/merchant", "/auth", "/api/auth"})
public class MerchantController {

    @Autowired
    private MerchantUserRepository merchantUserRepository;

    @PostMapping("/login")
    public Result<MerchantUser> login(@RequestBody(required = false) Map<String, String> body) {
        if (body == null) {
            return Result.error("请输入账号和密码");
        }
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return Result.error("账号或密码不能为空");
        }

        return merchantUserRepository.findByUsernameAndPassword(username, password)
                .map(u -> Result.success("商家登录成功", u))
                .orElseGet(() -> Result.error("账号或密码错误 (默认演示账号: admin / admin123)"));
    }
}
