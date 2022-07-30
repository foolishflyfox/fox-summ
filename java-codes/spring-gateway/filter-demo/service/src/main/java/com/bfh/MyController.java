package com.bfh;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Random;

/**
 * @author benfeihu
 */
@RestController
public class MyController {
    @GetMapping("/header")
    public String header(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder result = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String k = headerNames.nextElement();
            result.append(k);
            result.append(" = ");
            result.append(request.getHeader(k));
            result.append("\n");
        }
        return result.toString();
    }

    @GetMapping("/response")
    public String response(HttpServletResponse response) {
        response.addHeader("age", "18");
        response.addHeader("age", "18");
        return "hello";
    }

    @GetMapping("/path/**")
    public String path(HttpServletRequest request) {
        return "path = " + request.getRequestURI() + "\n";
    }

    @GetMapping("/params")
    public String params(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder result = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            String parameter = parameterNames.nextElement();
            result.append(String.format("%s = %s%n", parameter, request.getParameter(parameter)));
        }
        return result.toString();
    }

    @GetMapping("/status")
    public String status() {
        try {
            Thread.sleep(new Random().nextInt(1000) + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello\n";
    }

    @GetMapping("/foo")
    public String foo() {
        return "hello, foo";
    }

    @GetMapping("/bar")
    public String bar() {
        return "hello, bar";
    }

    @GetMapping("/baz")
    public String baz() {
        return "hello, baz";
    }
}
