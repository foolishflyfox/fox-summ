# Spring Cloud Gateway 断言的使用

Spring Cloud Gateway 的使用非常简单，只需要3步。

1. 引入 SpringCloud Gateway 库
```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
```

2. 写一个 SpringBoot 的主程序
```java
@SpringBootApplication
public class GateWayMain {
    public static void main(String[] args) {
        SpringApplication.run(GateWayMain.class, args);
    }
}
```

3. 配置 application.xml，例如：
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: foo-route
          uri: http://localhost:2001
          predicates:
            - Path=/foo/**,/g*/*
server:
  port: 2000
```

表示 predicates 匹配到的 url 都转到 localhost:2001 服务上去。该服务的代码非常简单：
```java
@RestController
public class MyController {
    @Value("${server.port}")
    private Integer port;

    @RequestMapping(value = {"/*", "/*/*", "/*/*/*"},
            method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    public String all(HttpServletRequest request) {
        return String.format("Port = %d, path = %s%n", port,
                request.getRequestURI());
    }
}
```
启动两个服务后，测试的结果为：
```shell
$ curl localhost:2000/foo
Port = 2001, path = /foo
$ curl localhost:2000/foo/a
Port = 2001, path = /foo/a
$ curl localhost:2000/foo/a/b
Port = 2001, path = /foo/a/b
$ curl localhost:2000/fx     
{"timestamp":"2022-07-29T16:00:20.166+00:00","path":"/fx","status":404,"error":"Not Found","message":null,"requestId":"5e2954cc-12"}
$ curl localhost:2000/gx/a
Port = 2001, path = /gx/a
$ curl localhost:2000/gy/a
Port = 2001, path = /gy/a
$ curl localhost:2000/gy/a/b
{"timestamp":"2022-07-29T16:00:33.102+00:00","path":"/gy/a/b","status":404,"error":"Not Found","message":null,"requestId":"b0c7d0e3-15"}
```
结论：在 Path 中，`**` 表示匹配任意路径，`*` 表示匹配任意多个字符。

多个 predicates 是且的关系，同一个 predicate 的多个条件之间是或的关系，例如将配置改为：
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: foo-route
          uri: http://localhost:2001
          predicates:
            - Path=/foo/**
            - Method=GET,POST
```
则实验结果为：
```shell
# 满足 GET && /foo
$ curl -X GET localhost:2000/foo
Port = 2001, path = /foo

# 满足 POST && /foo
$ curl -X POST localhost:2000/foo
Port = 2001, path = /foo

# 只满足 POST
$ curl -X POST localhost:2000/fo 
{"timestamp":"2022-07-29T16:18:29.851+00:00","path":"/fo","status":404,"error":"Not Found","message":null,"requestId":"4430a0d1-4"}

# 只满足 /foo
$ curl -X PUT localhost:2000/foo
{"timestamp":"2022-07-29T16:18:36.895+00:00","path":"/foo","status":404,"error":"Not Found","message":null,"requestId":"cef8e41c-5"}
```


