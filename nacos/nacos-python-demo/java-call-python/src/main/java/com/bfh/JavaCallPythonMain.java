package com.bfh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author benfeihu
 */
@SpringBootApplication
@EnableFeignClients
public class JavaCallPythonMain implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(JavaCallPythonMain.class, args);
    }

    @Autowired
    TestService testService;

    @Override
    public void run(String... args) throws Exception {
        User user = testService.foo();
        System.out.println(String.format("用户名: %s%n年龄: %d%n爱好: %s%n",
                user.getName(), user.getAge(), String.join(", ", user.getHobbies())));
        System.out.println(testService.bar("foolishflyfox"));
    }
}
