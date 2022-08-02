# 在 Java 中使用 auth0 的 JWT 库

## 介绍

本教程主要介绍 Auth0 的 JWT 库的使用。

## JWT 加入项目

使用 gradle 的用户可以使用：

```gradle
implementation 'com.auth0:java-jwt:3.14.0'
```

使用 Maven 的用户可以使用：

```xml
<dependency>
  <groupId>com.auth0</groupId>
  <artifactId>java-jwt</artifactId>
  <version>3.14.0</version>
</dependency>
```

auth0 的 github 源码地址为 [auth0/java-jwt](https://github.com/auth0/java-jwt) 。

## 生成 JSON Web Token

下面的例子演示了基于两个声明的值 username 和 role 生成 JWT。

```java
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class GenerateJWTExample {
    public static void main(String[] args) {
        String secret = "123@abc";
        Algorithm algorithm = Algorithm.HMAC512(secret);

        String generateToke = JWT.create()
                .withIssuer("Simple Solution")
                .withClaim("username", "TestX")
                .withClaim("role", "superman")
                .sign(algorithm);

        System.out.println(generateToke);
    }
}
```
运行结果为：
```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoic3VwZXJtYW4iLCJpc3MiOiJTaW1wbGUgU29sdXRpb24iLCJ1c2VybmFtZSI6IlRlc3RYIn0.Iw-sXpPXPjX2p8eyXM2V5ikWbDwbTQjGqDrZC4e76Zf3m6DYbSfGD3TIiQGOhwv6lpsuFowM7YWOzSUORDecDQ
```

下面的 Java 程序生成一个过期时间为1分钟的JWT。

```java
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class GenerateJWTWithExpireExample {
    public static void main(String[] args) {
        String secret = "123@abc";
        Algorithm algorithm = Algorithm.HMAC512(secret);
        long expireTime = (new Date().getTime()) + 60000;
        Date expirtDate = new Date(expireTime);

        String generateToken = JWT.create()
                .withIssuer("fff")
                .withClaim("username", "TestX")
                .withClaim("role", "superman")
                .withExpiresAt(expirtDate)
                .sign(algorithm);
        
        System.out.println(generateToken);
    }
}
```
输出结果为：
```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoic3VwZXJtYW4iLCJpc3MiOiJmZmYiLCJleHAiOjE2NTk0MzEwOTEsInVzZXJuYW1lIjoiVGVzdFgifQ.oEAadwR8H1GKNybokbMypGxeywf081GhCzaDHidZo7BJXPZaG-NVhxwbRuNE43ThQYFZubFgvjp9sFGykqpQdQ
```

## 校验 jwt

校验不带过期时间的 token。
```java
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class VerifyJWTExample {
    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoic3VwZXJtYW4iLCJpc3MiOiJTaW1wbGUgU29sdXRpb24iLCJ1c2VybmFtZSI6IlRlc3RYIn0.Iw-sXpPXPjX2p8eyXM2V5ikWbDwbTQjGqDrZC4e76Zf3m6DYbSfGD3TIiQGOhwv6lpsuFowM7YWOzSUORDecDQ";
        String secret = "123@abc";
        Algorithm algorithm = Algorithm.HMAC512(secret);

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Simple Solution")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            System.out.println("Verify JWT token success");
            System.out.println("Claims: " + decodedJWT.getClaims());
        } catch (JWTVerificationException ex) {
            System.out.println("Verify JWT token fail: " + ex.getMessage());
        }
    }
}
```
执行结果为：
```
Verify JWT token success
Claims: {iss="Simple Solution", role="superman", username="TestX"}
```

## 校验带过期时间的 JWT

```java
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class VerifyJWTWithExpireExample {
    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoic3VwZXJtYW4iLCJpc3MiOiJmZmYiLCJleHAiOjE2NTk0MzEwOTEsInVzZXJuYW1lIjoiVGVzdFgifQ.oEAadwR8H1GKNybokbMypGxeywf081GhCzaDHidZo7BJXPZaG-NVhxwbRuNE43ThQYFZubFgvjp9sFGykqpQdQ";
        String secret = "123@abc";
        Algorithm algorithm = Algorithm.HMAC512(secret);

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("fff")
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            System.out.println("verify jwt success");
            System.out.println(decodedJWT.getClaims());
        } catch (JWTVerificationException jwt) {
            System.out.println("verify jwt token fail: " + jwt.getMessage());
        }
    }
}
```
如果 JWT 已经过期，将抛出一个异常：
```
verify jwt token fail: The Token has expired on Tue Aug 02 17:04:51 CST 2022.
```
如果 JWT 未过期，显示为：
```
verify jwt success
{iss="fff", role="superman", exp=1659432430, username="TestX"}
```

## 解码 JWT

```java
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class DecodeJWTExample {
    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoic3VwZXJtYW4iLCJpc3MiOiJTaW1wbGUgU29sdXRpb24iLCJ1c2VybmFtZSI6IlRlc3RYIn0.Iw-sXpPXPjX2p8eyXM2V5ikWbDwbTQjGqDrZC4e76Zf3m6DYbSfGD3TIiQGOhwv6lpsuFowM7YWOzSUORDecDQ";

        DecodedJWT decode = JWT.decode(token);

        System.out.println("Issuer: " + decode.getIssuer());
        System.out.println("Claims: " + decode.getClaims());
    }
}
```
执行结果为：
```
Issuer: Simple Solution
Claims: {iss="Simple Solution", role="superman", username="TestX"}
```

## 实现一个可复用的 JWTService 工具类

```java
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTService {

    private long DEFAULT_EXPIRE_IN_SECONDS = 60;
    private String secret = "123@abc";
    private Algorithm algorithm = Algorithm.HMAC512(secret);

    public String generateJWTToken(String username, String role) {
        long now = new Date().getTime();
        long expireTime = now + (DEFAULT_EXPIRE_IN_SECONDS * 1000);
        Date expireDate = new Date(expireTime);

        String jwtToken = JWT.create()
                .withIssuer("Simple Solution")
                .withClaim("username", username)
                .withClaim("role", role)
                .withExpiresAt(expireDate)
                .sign(algorithm);

        return jwtToken;
    }

    public boolean verifyJWTToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Simple Solution")
//                .acceptExpiresAt(DEFAULT_EXPIRE_IN_SECONDS)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getClaimFromToken(String token, String claimKey) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim(claimKey).toString();
    }
}
```
使用该工具类：
```java
public class JWTExample {
    public static void main(String[] args) {
        JWTService jwtService = new JWTService();
        String token = jwtService.generateJWTToken("TestUser", "User");
        boolean result = jwtService.verifyJWTToken(token);

        System.out.println("Generated token: " + token);
        System.out.println("Verify Result: " + result);
        System.out.println("Token Claim, username: " +
                jwtService.getClaimFromToken(token, "username"));
        System.out.println("Token Claim, role: " +
                jwtService.getClaimFromToken(token, "role"));
    }
}
```
执行结果为：
```
Generated token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVXNlciIsImlzcyI6IlNpbXBsZSBTb2x1dGlvbiIsImV4cCI6MTY1OTQzMzU2NiwidXNlcm5hbWUiOiJUZXN0VXNlciJ9.0YrWgoHQTfwQNgvE0-yjZn613V2OzT116OkFuW7XJ3LBd6OonfWuo-KNYMedwGJxgSpsN_qQ8K3xINh8YopUMQ
Verify Result: true
Token Claim, username: "TestUser"
Token Claim, role: "User"
```