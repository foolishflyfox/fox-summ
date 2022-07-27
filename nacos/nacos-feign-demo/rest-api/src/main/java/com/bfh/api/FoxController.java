package com.bfh.api;

import com.bfh.api.bean.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/fox")
@FeignClient(name = "rest-provider")
public interface FoxController {
    @GetMapping("/say")
    String say();

    @GetMapping("/user")
    User queryUser(@RequestParam("name") String name);
}
