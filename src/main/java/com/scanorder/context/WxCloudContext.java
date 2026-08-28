package com.scanorder.context;

import lombok.Data;

/**
 * 微信云托管线程上下文对象 (保存微信云托管网关注入的 OpenID / UnionID 等信息)
 */
@Data
public class WxCloudContext {

    private static final ThreadLocal<WxCloudContext> CONTEXT_HOLDER = new ThreadLocal<>();

    private String openId;
    private String unionId;
    private String fromOpenId;
    private String source;
    private String env;

    public static WxCloudContext get() {
        WxCloudContext ctx = CONTEXT_HOLDER.get();
        if (ctx == null) {
            ctx = new WxCloudContext();
            CONTEXT_HOLDER.set(ctx);
        }
        return ctx;
    }

    public static void set(WxCloudContext context) {
        CONTEXT_HOLDER.set(context);
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
