package com.example.springbootdemothree.service;

import com.example.springbootdemothree.domain.Ebook;
import com.example.springbootdemothree.mapper.EbookMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list () {
        return ebookMapper.selectByExample(null);
    }
}
