package com.example.springbootdemothree.controller;

import com.example.springbootdemothree.domain.Ebook;
import com.example.springbootdemothree.service.EbookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public List<Ebook> list () {
        return ebookService.list();
    }
}
