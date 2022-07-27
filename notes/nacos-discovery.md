# nacos 服务发现

总结：

1. 服务提供方引入 nacos discovery 包，将服务注册到注册中心
2. 定义服务交互接口，需要使用 `@FeignClient` 注解
3. 服务消费方引入 nacos discovery 包，并在 spring boot 的 main 方法所在类添加 `@EnableFeignClients`，通过 `@Autowire` 引入接口，即可在本地使用

## nacos + openFeign 服务发现

代码 【nacos / nacos-feign-demo】。

### feign 接口定义

为了避免服务消费方重复定义服务接口，在一个公共包中，公共包定义了一个接口：
```java
package com.bfh.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/fox")
@FeignClient(name = "rest-provider")  // 指定服务方名，在 nacos 中用于服务发现
public interface FoxController {
    @GetMapping("/say")
    String say();
}
```

### 服务提供方

服务提供方的 restful 接口实现继承自该接口：
```java
package com.bfh.controller;

import com.bfh.api.FoxController;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoxControllerImpl implements FoxController {
    @Override
    public String say() {
        return "hello, fox\n";
    }
}
```
对于服务提供方而言，在代码上其实是不需要感知 feign 的，只需要按正常的 web 服务写代码即可。不过需要引入 nacos-discovery 包，将服务发布到 nacos 上。

### 服务消费方

服务消费方需要在 springboot 主程序上添加 `@EnableFeignClients`，才能通过动态代理机制实例化 openfeign 接口。

通过 `@Autowire` 引入之前定义的接口即可使用：
```java
package com.bfh.controller;

import com.bfh.api.FoxController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired  // 通过 Autowired 引入 feign client 接口
    FoxController foxController;

    @GetMapping("/t1")
    public String t1() {
        return foxController.say();  // 直接使用接口即可
    }
}
```

