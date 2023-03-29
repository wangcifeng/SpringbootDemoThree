package com.example.springbootdemothree.controller;

import com.example.springbootdemothree.req.EbookReq;
import com.example.springbootdemothree.resp.CommonResp;
import com.example.springbootdemothree.resp.EbookResp;
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
    public CommonResp list (EbookReq req) {
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return  resp;
    }
}
