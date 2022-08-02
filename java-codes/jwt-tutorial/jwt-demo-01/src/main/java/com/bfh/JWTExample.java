package com.bfh;

public class JWTExample {
    public static void main(String[] args) {
        JWTService jwtService = new JWTService();
        String token = jwtService.generateJWTToken("TestUser", "User");
        boolean result = jwtService.verifyJWTToken(token);

        System.out.println("Generated token: " + token);
        System.out.println("Verify Result: " + result);
        System.out.println("Token Claim, username: " +
                jwtService.getClaimFromToken(token, "username"));
        System.out.println("Token Claim, role: " +
                jwtService.getClaimFromToken(token, "role"));
    }
}
