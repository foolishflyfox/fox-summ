package com.bfh;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.*;

public class TokenGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenGatewayFilterFactory.Config> {

    /**
     * 用于设置配置参数，如果不设置，将不能从 yaml 文件获取配置
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("withAuth");
    }

    public TokenGatewayFilterFactory() {
        // 需要调用一下父类
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            String username = exchange.getRequest().getHeaders().getFirst("username");
            String password = exchange.getRequest().getHeaders().getFirst("password");
            // pre 调用
            exchange.getResponse().getHeaders().set("startToken", TokenGatewayFilterFactory.now());
            if (config.isWithAuth() && !isValidUser(username, password)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // post 时调用
                exchange.getResponse().getHeaders().set("endToken", TokenGatewayFilterFactory.now());
            }));
        };
    }

    /**
     * 校验逻辑，应该根据业务功能自定义其中的代码，被apply函数调用
     */
    private boolean isValidUser(String username, String password) {
        Map<String, String> users = new HashMap<>();
        users.put("fff", "123");
        users.put("abc", "000");
        if (!users.containsKey(username)) return false;
        return users.get(username).equals(password);
    }

    static public String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    /**
     * 配置类，在 yaml 中的 filters 设置中配置 Token: true，则 withAuth 值为true
     */
    @Data
    public static class Config {
        private boolean withAuth;
    }
}
