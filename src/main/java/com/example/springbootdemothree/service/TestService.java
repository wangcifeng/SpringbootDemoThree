package com.example.springbootdemothree.service;

import com.example.springbootdemothree.domain.Test;
import com.example.springbootdemothree.mapper.TestMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Resource
    private TestMapper testMapper;

    public List<Test> list() {
        return testMapper.list();
    }
}
