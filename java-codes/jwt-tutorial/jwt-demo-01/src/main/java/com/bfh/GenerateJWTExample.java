package com.bfh;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * @author benfeihu
 */
public class GenerateJWTExample {
    public static void main(String[] args) {
        String secret = "123@abc";
        Algorithm algorithm = Algorithm.HMAC512(secret);

        String generateToke = JWT.create()
                .withIssuer("Simple Solution")
                .withClaim("username", "TestX")
                .withClaim("role", "superman")
                .sign(algorithm);

        System.out.println(generateToke);
    }
}
