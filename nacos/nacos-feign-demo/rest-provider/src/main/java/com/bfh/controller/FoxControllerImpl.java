package com.bfh.controller;

import com.bfh.api.FoxController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author benfeihu
 */
@RestController
public class FoxControllerImpl implements FoxController {
    @Override
    public String say() {
        return "hello, fox\n";
    }
}
