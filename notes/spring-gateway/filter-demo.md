# Spring Cloud Gateway 过滤器的使用

过滤器的使用只需要在 spring.cloud.gateway.routes 的元素中添加 filters 即可。

## gateway 内置过滤器

- Header 请求头类过滤器
    - 添加字段: `AddRequestHeader=name,fff`
    - 删除字段: `RemoveRequestHeader=user-agent`
- Response 响应过滤器(curl 通过 -i 可查看响应)
    - 添加字段: `AddResponseHeader=school,ustc`
    - 删除字段: `RemoveResponseHeader=Date`
    - 修改字段: `SetResponseHeader=pos,shh`，注意，无则添加，有则修改
    - 重复字段去重: `DedupeResponseHeader=age`
- Param 参数
    - 添加请求参数: `AddRequestParameter=foo, bar`
- Status
    - 设置响应码: `SetStatus=404`
- Path 路径
    - 添加路径前缀: `PrefixPath=/path`，则 `curl localhost:2001/path/a/bb/c` 的路径变为了 `/path/a/bb/c`
    - 路径重写: `RewritePath=/apple/(?<segment>.*),/$\{segment}` 表示将 `/apple/path/1` 路径变为 `/path/1`
    - 路径设置: 下面的代码也能完成去重前缀的效果
    - 去除部分路径: `StripPrefix=1`，去除第一个路径
```yaml
- id: test-set-path
    uri: http://localhost:2001
    predicates:
    -  Path=/apple/{p1}
    filters:
    - SetPath=/{p1}
```

## 以代码的形式进行配置

例如下面的代码：
```java
@SpringBootApplication
public class CodeGatewayMain {
    public static void main(String[] args) {
        SpringApplication.run(CodeGatewayMain.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(r -> r.path("/header")
                        .filters(f -> f.addRequestHeader("name", "fff"))
                        .uri("http://localhost:2001")
                        .id("change-header"))
                .route(r -> r.path("/test/**")
                        .filters(f -> f.prefixPath("/path"))
                        .uri("http://localhost:2001")
                        .id("add-prefix"))
                .build();
    }
}
```
对应的配置为：
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: change-header
          uri: http://localhost:2001
          predicates:
            - Path=/header
          filters:
            - AddRequestHeader=name, fff
        - id: add-prefix
          uri: http://localhost:2001
          predicates:
            - Path=/test/**
          filters:
            - PrefixPath=/path
```
另外，在 application.yaml 中配置的路由规则同样生效，默认通过代码配置的路由优先级高于通过 yaml 文件配置的路由优先级，可以在代码中通过 `order` 函数降低优先级：`.route(r -> r.path("/").id("xxx").order(10000))`。


## 自定义过滤器

自定义过滤器需要继承自 Order 和 GatewayFilter 接口。
```java
package com.bfh;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFilter implements Ordered, GatewayFilter {

    static private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    static public String getTimestamp() {
        return sdf.format(new Date());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // pre Filter 执行
        exchange.getResponse().getHeaders().add("enterTimestamp", MyFilter.getTimestamp());
        // post Filter 执行
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    exchange.getResponse().getHeaders().add("leaveTimestamp", MyFilter.getTimestamp());
                })
        );
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
```

## 使用自定义过滤器

```java
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(r -> r.path("/status")
                        .filters(f -> f.filter(new MyFilter()))
                        .uri("http://localhost:2001")
                        .id("myfilter"))
                .build();
    }
```
则上面自定义的过滤器就能针对 `/status` 路径生效了。
```shell
$ curl localhost:2000/status -i
HTTP/1.1 200 OK
enterTimestamp: 2022-07-30 14:46:31.510     # 开始时间
Content-Type: text/plain;charset=UTF-8
Content-Length: 6
Date: Sat, 30 Jul 2022 06:46:32 GMT
leaveTimestamp: 2022-07-30 14:46:32.084     # 结束时间

hello
```

## 使用全局过滤器

全局自定义过滤器与局部自定义过滤器的区别仅仅是在定义过滤器时所继承的类。

局部自定义过滤器继承自 `GatewayFilter`，全局自定义过滤器继承自 `GlobalFilter`。

```java
public class MyGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求前执行的内容
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (!"fff".equals(token)) {  // 校验token
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
```

设置全局过滤器：
```java
@Bean
MyGlobalFilter myGlobalFilter() {
    return new MyGlobalFilter();
}
```

测试结果为：
```shell
$ curl -i -H "token:abc" http://localhost:2000/status 
HTTP/1.1 401 Unauthorized
content-length: 0

$ curl -i -H "token:fff" http://localhost:2000/status 
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8
Content-Length: 6
Date: Sat, 30 Jul 2022 07:36:44 GMT

hello
```

## 自定义过滤器工厂


