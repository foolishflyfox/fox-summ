package com.bfh.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/fox")
@FeignClient(name = "rest-provider")
public interface FoxController {
    @GetMapping("/say")
    String say();
}
