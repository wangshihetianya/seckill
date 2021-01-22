package com.zhl.sportplay.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zhl.sportplay.pojo.Token;

import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String TOKEN_SECRET  = "dDFH76hgfkahg768a";

    private static final long EXPIRE_TIME  = 30 * 60 * 1000;

    private static Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);


    /**
     * 根据用户id,名称和角色来创建Token
     * @param id
     * @param username
     * @param role
     * @return
     */
    public static String createToken(int id, String username, String role) {

        //过期时间和加密算法设置
        Date date=new Date(System.currentTimeMillis()+EXPIRE_TIME);
//        Algorithm algorithm =Algorithm.HMAC256(TOKEN_SECRET);

        //头部信息
        Map<String,Object> header=new HashMap<>(2);
        header.put("typ","JWT");
        header.put("alg","HS256");

        return JWT.create()
                .withHeader(header)
                .withClaim("id",id)
                .withClaim("username",username)
                .withClaim("role",role)
                .withClaim("lastLogin",new Date())
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 解析并获取token里的值
     * @param token
     * @param attribute
     * @return
     */
    public static String getTokenData(String token, String attribute) {

        DecodedJWT decodedJWT = JWT.decode(token);

        return decodedJWT.getClaim(attribute).asString();

    }




}
