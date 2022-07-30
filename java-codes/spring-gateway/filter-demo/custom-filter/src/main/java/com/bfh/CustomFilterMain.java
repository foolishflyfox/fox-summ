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
public class CustomFilterMain {
    public static void main(String[] args) {
        SpringApplication.run(CustomFilterMain.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(r -> r.path("/status")
                        .filters(f -> f.filter(new MyFilter()))
                        .uri("http://localhost:2001")
                        .id("myfilter"))
                .build();
    }
}
