package com.cinsc.MainView.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cinsc.MainView.constant.MainViewConstant;
import com.cinsc.MainView.enums.ResultEnum;
import com.cinsc.MainView.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
public class JWTUtil {

    // 过期时间5分钟
    private static final long EXPIRE_TIME = MainViewConstant.TOKEN_EXPIRE_TIME;

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param userId 用户账户
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, Integer userId, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userId", userId)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch(TokenExpiredException e){
            log.warn("[校验token是否正确] token过时");
//            throw new SystemException(ResultEnum.TOKEN_EXPIRE);
            return false;

        } catch (UnsupportedEncodingException e) {
            log.warn("[校验token是否正确] token解码异常");
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @param token 密钥
     * @return token中包含的用户名
     */
    public static Integer getUserId(String token) {

        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asInt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 生成签名,5min后过期
     * @param userId 用户账户
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String sign(Integer userId, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("userId", userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
