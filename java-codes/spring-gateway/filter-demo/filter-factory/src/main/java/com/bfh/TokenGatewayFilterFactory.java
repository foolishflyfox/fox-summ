package com.bfh;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author benfeihu
 */
public class TokenGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenGatewayFilterFactory.Config> {


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String username = exchange.getRequest().getHeaders().getFirst("username");
            String password = exchange.getRequest().getHeaders().getFirst("password");
            if (config.isWithAuth() && !isValidUser(username, password)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }

    public TokenGatewayFilterFactory() {
        // 需要调用一下父类
        super(Config.class);
    }

    private boolean isValidUser(String username, String password) {
        Map<String, String> users = new HashMap<>();
        users.put("fff", "123");
        users.put("abc", "000");
        if (!users.containsKey(username)) return false;
        return users.get(username).equals(password);
    }

    @Data
    public static class Config {
        private boolean withAuth;
    }
}
