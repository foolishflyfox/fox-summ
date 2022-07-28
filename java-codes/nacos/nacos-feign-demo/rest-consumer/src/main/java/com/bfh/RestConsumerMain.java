package com.bfh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author benfeihu
 */
@SpringBootApplication
@EnableFeignClients
public class RestConsumerMain {
    public static void main(String[] args) {
        SpringApplication.run(RestConsumerMain.class, args);
    }
}
