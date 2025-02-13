package com.atguigu.lease.common.constant;

public class RedisConstant {
    //后台管理系统
    //验证码redis键值对
    public static final String ADMIN_LOGIN_PREFIX = "admin:login:";
    //验证码过期时间
    public static final Integer ADMIN_LOGIN_CAPTCHA_TTL_SEC = 60;

    //移动端
    public static final String APP_LOGIN_PREFIX = "app:login:";
    public static final Integer APP_LOGIN_CODE_RESEND_TIME_SEC = 60;
    public static final Integer APP_LOGIN_CODE_TTL_SEC = 60 * 10;
    public static final String APP_ROOM_PREFIX = "app:room:";

}
