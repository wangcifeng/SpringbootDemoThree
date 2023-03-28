package com.example.springbootdemothree.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @GetMapping("/hello")
    //@RequestMapping(value = "/user/1",method = RequestMethod.GET)
    public String hello() {
        return "hello word!";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "hello word!" + name;
    }
}
