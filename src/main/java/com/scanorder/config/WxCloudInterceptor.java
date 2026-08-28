package com.scanorder.config;

import com.scanorder.context.WxCloudContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 微信云托管请求头拦截器 (自动解析微信注入的身份头 x-wx-openid, x-wx-unionid)
 */
@Component
public class WxCloudInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        WxCloudContext ctx = new WxCloudContext();
        
        // 读取微信云托管专属请求头 (免鉴权直接获取当前访问小程序用户的 OpenID)
        String wxOpenId = request.getHeader("x-wx-openid");
        String wxUnionId = request.getHeader("x-wx-unionid");
        String wxFromOpenId = request.getHeader("x-wx-from-openid");
        String wxSource = request.getHeader("x-wx-source");
        String wxEnv = request.getHeader("x-wx-env");

        ctx.setOpenId(wxOpenId);
        ctx.setUnionId(wxUnionId);
        ctx.setFromOpenId(wxFromOpenId);
        ctx.setSource(wxSource);
        ctx.setEnv(wxEnv);

        WxCloudContext.set(ctx);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        WxCloudContext.clear();
    }
}
