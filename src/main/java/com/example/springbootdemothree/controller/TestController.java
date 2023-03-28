package com.example.springbootdemothree.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @Value("${test.hello:TOME}")
    private String helloTest;

    @GetMapping("/hello")
    //@RequestMapping(value = "/user/1",method = RequestMethod.GET)
    public String hello() {
        return "hello word!" + helloTest;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "hello word!" + name;
    }
}
