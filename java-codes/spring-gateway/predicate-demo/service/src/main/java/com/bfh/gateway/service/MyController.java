package com.bfh.gateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author benfeihu
 */
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
