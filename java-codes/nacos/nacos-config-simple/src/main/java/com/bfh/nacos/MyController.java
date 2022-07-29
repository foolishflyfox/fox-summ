package com.bfh.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author benfeihu
 */
@RestController
@RefreshScope
public class MyController {
    @Value("${demo.name}")
    private String demoName;

    @GetMapping("/foo")
    public String foo() {
        return demoName;
    }
}
