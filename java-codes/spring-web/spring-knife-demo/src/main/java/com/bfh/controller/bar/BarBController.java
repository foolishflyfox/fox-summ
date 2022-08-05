package com.bfh.controller.bar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author benfeihu
 */
@RestController
@RequestMapping("/bar")
public class BarBController {
    @GetMapping("t1")
    public String t1() {
        return "this is bar t1";
    }
}
