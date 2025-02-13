package com.atguigu.lease.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {
    private static SecretKey secretKey = Keys.hmacShaKeyFor("a231231yyiLCS12wqeyqweiugsadb121371234892748sjkdahsdjas".getBytes());
    public static String createToken(Long userId, String username){

        String Jwt = Jwts.builder().
//                设置过期时间
        setExpiration(new Date(System.currentTimeMillis() + 3600000)).
                setSubject("LOGIN_USER").
                claim("userId", userId).
//                claim声明自己定义的方法
        claim("username", username).
                //设置签名算法 HS系列
                        signWith(secretKey, SignatureAlgorithm.HS256).
                compact();
        return Jwt;

    }




//    测试方法
    public static void main(String[] args) {
        String token = createToken(2L, "zhangsan");
        System.out.println(token);
    }
}
