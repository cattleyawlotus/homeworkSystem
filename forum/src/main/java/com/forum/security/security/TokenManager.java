package com.forum.security.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {
    //token有效时长
    private static long tokenExpiration = 24*60*60*1000;
    //编码密钥
    private static String tokenSignKey = "123456";

    //1.使用jwt根据用户名生产token
    public static String createToken(String username) {
        String token = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }
    //2.根据token字符串得到用户信息
    public static String getUserFromToken(String token) {
        String user = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return user;
    }
    //3.删除token
    public static void removeToken(String token) {
        //jwttoken 无需删除，客户端扔掉即可。
    }
}
