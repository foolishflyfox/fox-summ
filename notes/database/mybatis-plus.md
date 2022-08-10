# mybatis 的使用

## 最简单的实践

### 引入依赖库

1. 引入对应数据库的驱动，例如我们使用的是 mysql，则 pom 中引入：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. 引入 mysql-plus 库:

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.1</version>
</dependency>
```

3. 引入 spring-boot-web 库:

```xml
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

4. 其他辅助库

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
    <scope>provided</scope>
</dependency>
```

### 配置 application.yaml

下面是一个最简的数据库源配置:

```yaml
spring:
  datasource:
    username: root
    password: 12345678
    url: jdbc:mysql://192.168.155.99:3306/mybatis_plus_demo
```

### 创建对象用于映射数据库

在数据库中有一个 user 表，建表 sql 如下:
```sql
CREATE DATABASE IF NOT EXISTS mybatis_plus_demo;
USE mybatis_plus_demo;
DROP TABLE IF EXISTS user;
CREATE TABLE `user` (`id` INT AUTO_INCREMENT, 
    `name` VARCHAR(32) DEFAULT NULL,
    `age` TINYINT,
    `email` VARCHAR(128),
    PRIMARY KEY(`id`)
);
INSERT INTO user(`name`, `age`, `email`) values("a", 18, "a@xyz.com");
INSERT INTO user(`name`, `age`, `email`) values("b", 19, "b@xyz.com");
INSERT INTO user(`name`, `age`, `email`) values("c", 20, "c@xyz.com");
```

创建与数据库对应的数据结构：

```java
@Data
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private String email;
}
```

### 创建 mapper 接口

```java
package com.bfh;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserMapper extends BaseMapper<User> {
}
```

### 使用 mybatis-plus 接口服务

```java
package com.bfh;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@MapperScan("com.bfh")  // 在该包中扫描需要 mapper 的接口(继承自BaseMapper)
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

    @Autowired
    UserMapper userMapper;

    @GetMapping("/id/{id}")
    public String queryById(@PathVariable("id")Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        return user.toString();
    }
}
```

启动后，访问结果为：
```sh
$ curl localhost:8080/id/1
User(id=1, name=a, age=18, email=a@xyz.com)
```

