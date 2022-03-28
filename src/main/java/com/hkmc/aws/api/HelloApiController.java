package com.hkmc.aws.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApiController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World!!";
    }
}
