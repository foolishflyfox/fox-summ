package com.bfh;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class VerifyJWTExample {
    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoic3VwZXJtYW4iLCJpc3MiOiJTaW1wbGUgU29sdXRpb24iLCJ1c2VybmFtZSI6IlRlc3RYIn0.Iw-sXpPXPjX2p8eyXM2V5ikWbDwbTQjGqDrZC4e76Zf3m6DYbSfGD3TIiQGOhwv6lpsuFowM7YWOzSUORDecDQ";
        String secret = "123@abc";
        Algorithm algorithm = Algorithm.HMAC512(secret);

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Simple Solution")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            System.out.println("Verify JWT token success");
            System.out.println("Claims: " + decodedJWT.getClaims());
        } catch (JWTVerificationException ex) {
            System.out.println("Verify JWT token fail: " + ex.getMessage());
        }
    }
}
