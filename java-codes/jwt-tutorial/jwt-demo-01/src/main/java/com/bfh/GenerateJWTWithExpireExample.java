package com.bfh;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

/**
 * @author benfeihu
 */
public class GenerateJWTWithExpireExample {
    public static void main(String[] args) {
        String secret = "123@abc";
        Algorithm algorithm = Algorithm.HMAC512(secret);
        long expireTime = (new Date().getTime()) + 60000;
        Date expirtDate = new Date(expireTime);

        String generateToken = JWT.create()
                .withIssuer("fff")
                .withClaim("username", "TestX")
                .withClaim("role", "superman")
                .withExpiresAt(expirtDate)
                .sign(algorithm);

        System.out.println(generateToken);
    }
}
