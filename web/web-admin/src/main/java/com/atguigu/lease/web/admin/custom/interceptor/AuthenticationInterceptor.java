package com.atguigu.lease.web.admin.custom.interceptor;

import com.atguigu.lease.common.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


// 自定义拦截器
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 携带access_token才不会被拦截
        String token = request.getHeader("access_token");

        JwtUtils.parseToken(token);

        return true;
    }
}
