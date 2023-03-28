package com.example.springbootdemothree.service;

import com.example.springbootdemothree.domain.Demo;
import com.example.springbootdemothree.mapper.DemoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {
    @Resource
    private DemoMapper demoMapper;

    public List<Demo> list() {
        return demoMapper.selectByExample(null);
    };

}
