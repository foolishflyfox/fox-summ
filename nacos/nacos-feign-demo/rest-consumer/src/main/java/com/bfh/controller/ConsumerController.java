package com.bfh.controller;

import com.bfh.api.FoxController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author benfeihu
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    FoxController foxController;

    @GetMapping("/t1")
    public String t1() {
        return foxController.say();
    }
}
