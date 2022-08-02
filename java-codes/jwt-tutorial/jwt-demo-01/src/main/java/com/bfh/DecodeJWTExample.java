package com.bfh;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class DecodeJWTExample {
    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoic3VwZXJtYW4iLCJpc3MiOiJTaW1wbGUgU29sdXRpb24iLCJ1c2VybmFtZSI6IlRlc3RYIn0.Iw-sXpPXPjX2p8eyXM2V5ikWbDwbTQjGqDrZC4e76Zf3m6DYbSfGD3TIiQGOhwv6lpsuFowM7YWOzSUORDecDQ";

        DecodedJWT decode = JWT.decode(token);

        System.out.println("Issuer: " + decode.getIssuer());
        System.out.println("Claims: " + decode.getClaims());
    }
}
