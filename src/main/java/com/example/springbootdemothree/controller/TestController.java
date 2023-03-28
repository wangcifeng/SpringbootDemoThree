package com.example.springbootdemothree.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hello")
//    @RequestMapping(value = "/user/1",method = RequestMethod.GET)
    public String hello() {
        return "hello word!";
    }
}
