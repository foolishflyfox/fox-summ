package com.bfh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author benfeihu
 */
@RestController
public class TestController {
    @GetMapping("/t1")
    public String t1(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        return String.format("姓名: %s%n年龄: %d%n", name, age);

    }
    @GetMapping("/t2")
    public String t2(@RequestParam(value = "name", defaultValue = "fff") String name,
                     @RequestParam("age") Integer age) {
        return String.format("姓名: %s%n年龄: %d%n", name, age);
    }

    @PostMapping("/t3")
    public String t3(@RequestParam(value = "name", defaultValue = "fff") String name,
                     @RequestParam("age") Integer age) {
        return String.format("姓名: %s%n年龄: %d%n", name, age);
    }

    @GetMapping(value = {"/t4/{v1}/{v2}/{v3}", "/t4/{v1}/{v2}"})
    public String t4(@PathVariable("v1") Integer v1,
                     @PathVariable("v2") Integer v2,
                     @PathVariable(value = "v3", required = false) Integer v3) {
        return String.format("v1 = %s, v2 = %s, v3 = %s%n", v1, v2, v3);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User implements Serializable {
        private String name;
        private Integer age;
    }

    @PostMapping("/t5")
    public String t5(@RequestBody User user) {
        return String.format("name = %s, age = %s%n", user.getName(), user.getAge());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User2 implements Serializable {
        private String name = "fff";
        private Integer age = 18;
    }

    @PostMapping("/t6")
    public String t6(@RequestBody User2 user) {
        return String.format("name = %s, age = %s%n", user.getName(), user.getAge());
    }
}
