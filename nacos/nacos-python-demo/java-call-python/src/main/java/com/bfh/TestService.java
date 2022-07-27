package com.bfh;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author benfeihu
 */
@FeignClient(name = "test-service")
public interface TestService {
    @GetMapping("foo")
    User foo();

    @GetMapping("/bar/{name}")
    String bar(@PathVariable("name") String name);

}
