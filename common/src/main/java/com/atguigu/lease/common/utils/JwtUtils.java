package com.atguigu.lease.common.utils;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {
    private static SecretKey secretKey = Keys.hmacShaKeyFor("a231231yyiLCS12qwertyuiop121371234892748sjkdahsdjas".getBytes());
    public static String createToken(Long userId, String username){

        String jwt = Jwts.builder().
                setExpiration(new Date(System.currentTimeMillis() + 3600000*24)).//  设置过期时间
                setSubject("LOGIN_USER").
                claim("userId", userId).//  claim声明自己定义的方法
                claim("username", username).
                //设置签名算法 HS系列
                signWith(secretKey, SignatureAlgorithm.HS256).
                compact();
        return jwt;
    }
    //就是读取token，查有没有这个用户？然后要注意是否过期？因为return没填写jwt
    public static Claims parseToken(String token){
        if (token==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        //解析token
        try{
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            //修改返回值
            return claimsJws.getBody();
        }catch (ExpiredJwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        }catch (JwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }


    //测试方法
    public static void main(String[] args) {
        String token = createToken(2L, "user");
        System.out.println(token);
    }
}
