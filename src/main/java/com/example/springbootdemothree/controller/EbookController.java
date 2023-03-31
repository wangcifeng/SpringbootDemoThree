package com.example.springbootdemothree.controller;

import com.example.springbootdemothree.req.EbookQueryReq;
import com.example.springbootdemothree.req.EbookSaveReq;
import com.example.springbootdemothree.resp.CommonResp;
import com.example.springbootdemothree.resp.EbookQueryResp;
import com.example.springbootdemothree.resp.PageResp;
import com.example.springbootdemothree.service.EbookService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list (@Valid EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return  resp;
    }

    @PostMapping ("/save")
    public CommonResp save (@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return  resp;
    }

    @DeleteMapping ("/delete/{id}")
    public CommonResp delete (@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return  resp;
    }
}
