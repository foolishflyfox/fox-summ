package com.bfh.controller.foo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author benfeihu
 */
@RestController
@RequestMapping("/foo")
public class FooAController {
    @GetMapping("/t1")
    public String t1() {
        return "this is foo.t1";
    }
}
