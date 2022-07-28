# SpringBoot web 的默认参数

【代码: spring-web/web-default-params 】


## RequestParam

主要说明 `@RequestParam` 中 `defaultValue` 的使用。

### Get

如果 `@RequestParam` 中没有指定 `defaultValue`，那么未传递必要参数时，服务器将返回 400 Bad Request 错误，例如我们的 controller 为：
```java
@RestController
public class TestController {
    @GetMapping("/t1")
    public String t1(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        return String.format("姓名: %s%n年龄: %d%n", name, age);

    }
}
```
启动服务后，通过 curl 访问的结果为：
```shell
$ curl http://localhost:8080/t1\?name\=fff\&age\=18
姓名: fff
年龄: 18

$ curl http://localhost:8080/t1\?name\=fff         
{"timestamp":"2022-07-28T06:10:55.487+00:00","status":400,"error":"Bad Request","message":"","path":"/t1"}

$ curl http://localhost:8080/t1\?age\=18  
{"timestamp":"2022-07-28T06:11:04.100+00:00","status":400,"error":"Bad Request","message":"","path":"/t1"}

$ curl http://localhost:8080/t1         
{"timestamp":"2022-07-28T06:11:07.545+00:00","status":400,"error":"Bad Request","message":"","path":"/t1"}
```

**为 name 添加 defaultValue:**
```java
    @GetMapping("/t2")
    public String t2(@RequestParam(value = "name", defaultValue = "fff") String name,
                     @RequestParam("age") Integer age) {
        return String.format("姓名: %s%n年龄: %d%n", name, age);
    }
```
那么 name 参数并不是必须的：
```shell
$ curl http://localhost:8080/t2\?name\=abc\&age\=18
姓名: abc
年龄: 18

$ curl http://localhost:8080/t2\?age\=18           
姓名: fff
年龄: 18

$ curl http://localhost:8080/t2\?name\=abc         
{"timestamp":"2022-07-28T06:14:06.676+00:00","status":400,"error":"Bad Request","message":"","path":"/t2"}

$ curl http://localhost:8080/t2           
{"timestamp":"2022-07-28T06:14:14.580+00:00","status":400,"error":"Bad Request","message":"","path":"/t2"}% 
```

### Post

Post 中 RequestParam 的使用与 Get 类似：
```java
    @PostMapping("/t3")
    public String t3(@RequestParam(value = "name", defaultValue = "fff") String name,
                     @RequestParam("age") Integer age) {
        return String.format("姓名: %s%n年龄: %d%n", name, age);
    }
```
实验结果为：
```shell
$ curl -X POST http://localhost:8080/t3\?age\=18
姓名: fff
年龄: 18
$ curl -X POST http://localhost:8080/t3\?name\=abc
{"timestamp":"2022-07-28T06:19:57.039+00:00","status":400,"error":"Bad Request","message":"","path":"/t3"}
$ curl -X POST http://localhost:8080/t3
{"timestamp":"2022-07-28T06:20:00.663+00:00","status":400,"error":"Bad Request","message":"","path":"/t3"}
```

## PathVariable

`PathVariable` 注解中的参数 `required` 就是表名该路径参数是否可以不设置。注意，如果路径参数不设置，对应的 url 路径是不一样的，因此需要设置多个路径。
```java
    public String t4(@PathVariable("v1") Integer v1,
                     @PathVariable("v2") Integer v2,
                     @PathVariable(value = "v3", required = false) Integer v3) {
        return String.format("v1 = %s, v2 = %s, v3 = %s%n", v1, v2, v3);
    }
```
执行结果为：
```shell
$ curl http://localhost:8080/t4/1/2/3
v1 = 1, v2 = 2, v3 = 3

$ curl http://localhost:8080/t4/1/2  
v1 = 1, v2 = 2, v3 = null
```

也就是说通过 PathVariable 可以指定某个参数不设置值。

## RequestBody

通过 RequestBody 从前端向后端传递数据时，如果对应的字段没有设置，则其值为 null：
```java
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
```

执行结果为：
```shell
$ curl -H "Content-Type: application/json" -d '{"name":"fff", "age":18}' http://localhost:8080/t5
name = fff, age = 18
$ curl -H "Content-Type: application/json" -d '{"name":"fff"}' http://localhost:8080/t5 
name = fff, age = null
$ curl -H "Content-Type: application/json" -d '{"age":18}' http://localhost:8080/t5
name = null, age = 18
$ curl -H "Content-Type: application/json" -d "{}" http://localhost:8080/t5 
name = null, age = null
```

如果不希望使用 null，而是有默认值，可以设置对象的默认值：
```java
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
```
执行结果为：
```
$ curl -H "Content-Type: application/json" -d "{}" http://localhost:8080/t6
name = fff, age = 18
$ curl -H "Content-Type: application/json" -d '{"name": "abc"}' http://localhost:8080/t6
name = abc, age = 18
$ curl -H "Content-Type: application/json" -d '{"age": 20}' http://localhost:8080/t6
name = fff, age = 20
```
