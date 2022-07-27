package com.bfh.controller;

import com.bfh.api.FoxController;
import com.bfh.api.bean.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoxControllerImpl implements FoxController {
    @Override
    public String say() {
        return "hello, fox\n";
    }

    @Override
    public User queryUser(String name) {
        return User.builder()
                .name(name)
                .age(18)
                .hobbies(new String[] {"basketball", "badminton", "cycling"})
                .build();
    }
}
