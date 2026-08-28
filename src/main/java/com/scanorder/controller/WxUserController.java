package com.scanorder.controller;

import com.scanorder.common.Result;
import com.scanorder.context.WxCloudContext;
import com.scanorder.entity.WxUser;
import com.scanorder.repository.WxUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 微信小程序用户登录与会员个人中心 API (微信云原生免密认证 + 模拟/真实登录无缝兼容)
 */
@RestController
@RequestMapping({"/user", "/api/user"})
public class WxUserController {

    @Autowired
    private WxUserRepository wxUserRepository;

    @PostMapping("/wx-login")
    public Result<Map<String, Object>> wxLogin(@RequestBody(required = false) Map<String, String> body) {
        String openId = null;
        String nickname = "微信食客";
        String avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=120";

        // 1. 优先从微信云托管注入的请求头获取真实 OpenID
        String headerOpenId = WxCloudContext.get().getOpenId();
        if (headerOpenId != null && !headerOpenId.trim().isEmpty()) {
            openId = headerOpenId;
        }

        // 2. 如果请求体传了 openId 或 nickname/avatarUrl
        if (body != null) {
            if (openId == null && body.containsKey("openId") && body.get("openId") != null && !body.get("openId").trim().isEmpty()) {
                openId = body.get("openId");
            }
            if (body.containsKey("nickname") && body.get("nickname") != null) {
                nickname = body.get("nickname");
            }
            if (body.containsKey("avatarUrl") && body.get("avatarUrl") != null) {
                avatarUrl = body.get("avatarUrl");
            }
        }

        if (openId == null || openId.trim().isEmpty()) {
            openId = "wx_openid_demo888";
        }

        final String finalOpenId = openId;
        final String finalNickname = nickname;
        final String finalAvatarUrl = avatarUrl;

        WxUser user = wxUserRepository.findByOpenId(finalOpenId).orElseGet(() -> {
            WxUser newUser = new WxUser();
            newUser.setId("usr_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
            newUser.setOpenId(finalOpenId);
            newUser.setNickname(finalNickname);
            newUser.setAvatarUrl(finalAvatarUrl);
            newUser.setMemberPoints(100);
            newUser.setMemberLevel("普通会员");
            newUser.setCreatedAt(LocalDateTime.now());
            return newUser;
        });

        user.setLastLoginAt(LocalDateTime.now());
        if (body != null && body.containsKey("nickname")) user.setNickname(finalNickname);
        if (body != null && body.containsKey("avatarUrl")) user.setAvatarUrl(finalAvatarUrl);
        wxUserRepository.save(user);

        Map<String, Object> res = new HashMap<>();
        res.put("token", "wx_jwt_token_" + user.getId());
        res.put("user", user);
        return Result.success("登录成功", res);
    }

    @GetMapping("/profile")
    public Result<WxUser> getProfile(@RequestParam(name = "openId", required = false) String openId) {
        if (openId == null || openId.trim().isEmpty()) {
            openId = WxCloudContext.get().getOpenId();
        }
        if (openId == null || openId.trim().isEmpty()) {
            openId = "wx_openid_demo888";
        }

        return wxUserRepository.findByOpenId(openId)
                .map(Result::success)
                .orElseGet(() -> {
                    WxUser demo = new WxUser();
                    demo.setId("usr_demo888");
                    demo.setOpenId("wx_openid_demo888");
                    demo.setNickname("寻味吃货小明");
                    demo.setAvatarUrl("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=120");
                    demo.setMemberPoints(350);
                    demo.setMemberLevel("黄金会员");
                    return Result.success(demo);
                });
    }

    @PostMapping("/update")
    public Result<WxUser> updateUserProfile(@RequestBody Map<String, Object> body) {
        String openId = (String) body.get("openId");
        if (openId == null) openId = WxCloudContext.get().getOpenId();
        if (openId == null) openId = "wx_openid_demo888";

        Optional<WxUser> userOpt = wxUserRepository.findByOpenId(openId);
        if (userOpt.isPresent()) {
            WxUser user = userOpt.get();
            if (body.containsKey("nickname")) user.setNickname((String) body.get("nickname"));
            if (body.containsKey("avatarUrl")) user.setAvatarUrl((String) body.get("avatarUrl"));
            if (body.containsKey("phone")) user.setPhone((String) body.get("phone"));
            wxUserRepository.save(user);
            return Result.success("个人信息更新成功", user);
        }
        return Result.error("用户不存在");
    }
}
