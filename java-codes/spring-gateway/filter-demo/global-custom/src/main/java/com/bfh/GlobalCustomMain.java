package com.bfh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GlobalCustomMain {
    public static void main(String[] args) {
        SpringApplication.run(GlobalCustomMain.class, args);
    }

    @Bean
    MyGlobalFilter myGlobalFilter() {
        return new MyGlobalFilter();
    }
}
