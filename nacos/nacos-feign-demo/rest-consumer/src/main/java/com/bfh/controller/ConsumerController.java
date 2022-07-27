package com.bfh.controller;

import com.bfh.api.FoxController;
import com.bfh.api.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    FoxController foxController;

    @GetMapping("/t1")
    public String t1() {
        return foxController.say();
    }

    @GetMapping("/t2/{name}/{id}")
    public String t2(@PathVariable("name") String name, @PathVariable("id") Integer id) {
        return "id = " + id + ", name = " + name;
    }

    @GetMapping("/user/{name}/{age}")
    public String queryUser(@PathVariable("age") Integer age, @PathVariable("name") String nm) {
//        return nm + " : " + age;
        User user = foxController.queryUser(nm);
        user.setAge(age);
        return String.format("姓名: %s%n年龄: %d%n爱好: %s%n", user.getName(), user.getAge(),
                String.join(", ", user.getHobbies()));
    }
}
