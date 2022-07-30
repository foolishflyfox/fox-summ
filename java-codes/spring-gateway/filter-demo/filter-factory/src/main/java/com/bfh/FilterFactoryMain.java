package com.bfh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author benfeihu
 */
@SpringBootApplication
public class FilterFactoryMain {
    public static void main(String[] args) {
        SpringApplication.run(FilterFactoryMain.class, args);
    }

    @Bean
    public TokenGatewayFilterFactory tokenGatewayFilterFactory() {
        return new TokenGatewayFilterFactory();
    }
}
