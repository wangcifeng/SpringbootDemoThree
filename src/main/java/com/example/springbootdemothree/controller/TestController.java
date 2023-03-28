package com.example.springbootdemothree.controller;

import com.example.springbootdemothree.domain.Test;
import com.example.springbootdemothree.service.TestService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {
    @Value("${test.hello:TOME}")
    private String helloTest;

    @Resource
    private TestService testService;

    @GetMapping("/hello")
    //@RequestMapping(value = "/user/1",method = RequestMethod.GET)
    public String hello() {
        return "hello word!" + helloTest;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "hello word!" + name;
    }

    @GetMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }
}
