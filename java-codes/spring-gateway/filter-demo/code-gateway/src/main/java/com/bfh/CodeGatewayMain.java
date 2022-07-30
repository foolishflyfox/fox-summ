package com.bfh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author benfeihu
 */
@SpringBootApplication
public class CodeGatewayMain {
    public static void main(String[] args) {
        SpringApplication.run(CodeGatewayMain.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(r -> r.path("/header")
                        .filters(f -> f.addRequestHeader("name", "fff"))
                        .uri("http://localhost:2001")
                        .id("change-header").order(10000))
                .route(r -> r.path("/test/**")
                        .filters(f -> f.prefixPath("/path"))
                        .uri("http://localhost:2001")
                        .id("add-prefix"))
                .build();
    }
}
