package com.bfh;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFilter implements Ordered, GatewayFilter {

    static private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    static public String getTimestamp() {
        return sdf.format(new Date());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // pre Filter 执行
        exchange.getResponse().getHeaders().add("enterTimestamp", MyFilter.getTimestamp());
        // post Filter 执行
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    exchange.getResponse().getHeaders().add("leaveTimestamp", MyFilter.getTimestamp());
                })
        );
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
