package com.bfh;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class VerifyJWTWithExpireExample {
    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoic3VwZXJtYW4iLCJpc3MiOiJmZmYiLCJleHAiOjE2NTk0MzI0MzAsInVzZXJuYW1lIjoiVGVzdFgifQ.MeMtHauN9hPoKzeO2pr0xkKTn1sg7xrc9sYe68wHS0-Xp9yLifnZXK2IYj7HJyF7wTvvv30wr-XGdaBUMb5x2w";
        String secret = "123@abc";
        Algorithm algorithm = Algorithm.HMAC512(secret);

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("fff")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            System.out.println("verify jwt success");
            System.out.println(decodedJWT.getClaims());
        } catch (JWTVerificationException jwt) {
            System.out.println("verify jwt token fail: " + jwt.getMessage());
        }
    }
}
