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

gateway 内置的过滤器，其实是一个个的过滤器工厂，下面我们也写一个简单的关于认证的过滤器工厂，并将其配置到 application.yaml 文件中完成认证功能。

### 创建自定义过滤器工厂

自定义过滤器工厂需要实现 AbstractGatewayFilterFactory 类，这个一个泛型抽象类。其中的泛型类型是一个配置类，用于接收来自 application.yaml 的关于过滤器的配置。配置类通常定义为过滤器工厂的一个内部类，并且定义 get 和 set 方法。

```java
package com.bfh;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.*;

public class TokenGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenGatewayFilterFactory.Config> {

    /**
     * 用于设置配置参数，如果不设置，将不能从 yaml 文件获取配置
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("withAuth");
    }

    public TokenGatewayFilterFactory() {
        // 需要调用一下父类
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            String username = exchange.getRequest().getHeaders().getFirst("username");
            String password = exchange.getRequest().getHeaders().getFirst("password");
            // pre 调用
            exchange.getResponse().getHeaders().set("startToken", TokenGatewayFilterFactory.now());
            if (config.isWithAuth() && !isValidUser(username, password)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // post 时调用
                exchange.getResponse().getHeaders().set("endToken", TokenGatewayFilterFactory.now());
            }));
        };
    }

    /**
     * 校验逻辑，应该根据业务功能自定义其中的代码，被apply函数调用
     */
    private boolean isValidUser(String username, String password) {
        Map<String, String> users = new HashMap<>();
        users.put("fff", "123");
        users.put("abc", "000");
        if (!users.containsKey(username)) return false;
        return users.get(username).equals(password);
    }

    static public String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    /**
     * 配置类，在 yaml 中的 filters 设置中配置 Token: true，则 withAuth 值为true
     */
    @Data
    public static class Config {
        private boolean withAuth;
    }
}
```

### 将自定义过滤器加入到 Spring 容器中

```java
    @Bean
    public TokenGatewayFilterFactory tokenGatewayFilterFactory() {
        return new TokenGatewayFilterFactory();
    }
```

### 在 application.yaml 中使用自定义过滤器

```yaml
server:
  port: 2000
spring:
  cloud:
    gateway:
      routes:
        - id: foo-path
          uri: http://localhost:2001
          predicates:
            - Path=/foo
          filters:
            - Token=true  # 自定义过滤器配置中出入的参数为 true
        - id: bar-path
          uri: http://localhost:2001
          predicates:
            - Path=/bar
          filters:
            - Token=false  # 自定义过滤器配置中传入的参数为 false
        - id: baz-path
          uri: http://localhost:2001
          predicates:
            - Path=/baz
```

### 验证

#### 验证自定义过滤器传入参数为 true

```shell
# foo 需要校验，没有传入用户名和密码，返回 401
$ curl -i http://localhost:2000/foo
HTTP/1.1 401 Unauthorized
startToken: 2022-08-01 08:18:05.376
content-length: 0

# 用户名或密码错误，返回 401
$ curl -i -H "username:fff" -H "password:000" http://localhost:2000/foo
HTTP/1.1 401 Unauthorized
startToken: 2022-08-01 08:19:22.582
content-length: 0

# 用户名和密码正确
$ curl -i -H "username:fff" -H "password:123" http://localhost:2000/foo
HTTP/1.1 200 OK
startToken: 2022-08-01 08:20:10.745
foo: 2022-08-01 08:20:11.164
Content-Type: text/plain;charset=UTF-8
Content-Length: 10
Date: Mon, 01 Aug 2022 00:20:11 GMT
endToken: 2022-08-01 08:20:11.216

hello, foo
```

#### 验证自定义过滤器传入参数为 false

```shell
# startToken 和 endToken 表名经过了完整的自定义过滤器，只是因为参数是 false，没进行认证
$ curl -i http://localhost:2000/bar
HTTP/1.1 200 OK
startToken: 2022-08-01 08:21:46.463
bar: 2022-08-01 08:21:46.485
Content-Type: text/plain;charset=UTF-8
Content-Length: 10
Date: Mon, 01 Aug 2022 00:21:46 GMT
endToken: 2022-08-01 08:21:46.490

hello, bar
```

#### 验证未设置自定义过滤器

```shell
# startToken 和 endToken 没有被设置，标明没有使用自定义过滤器
$ curl -i http://localhost:2000/baz                                    
HTTP/1.1 200 OK
baz: 2022-08-01 08:24:01.192
Content-Type: text/plain;charset=UTF-8
Content-Length: 10
Date: Mon, 01 Aug 2022 00:24:01 GMT

hello, baz
```

