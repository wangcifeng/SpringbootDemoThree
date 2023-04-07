package com.example.springbootdemothree.controller;

import com.example.springbootdemothree.domain.DocExample;
import com.example.springbootdemothree.req.DocQueryReq;
import com.example.springbootdemothree.req.DocSaveReq;
import com.example.springbootdemothree.resp.DocQueryResp;
import com.example.springbootdemothree.resp.CommonResp;
import com.example.springbootdemothree.resp.PageResp;
import com.example.springbootdemothree.service.DocService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {
    @Resource
    private DocService docService;

    @GetMapping("/list")
    public CommonResp list (@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return  resp;
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent (@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return  resp;
    }


    @GetMapping("/all")
    public CommonResp all () {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> all = docService.all();
        resp.setContent(all);
        return  resp;
    }

    @PostMapping ("/save")
    public CommonResp save (@RequestBody @Valid DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return  resp;
    }

//    @DeleteMapping ("/delete/{id}")
//    public CommonResp delete (@PathVariable Long id) {
//        CommonResp resp = new CommonResp<>();
//        docService.delete(id);
//        return  resp;
//    }

    @DeleteMapping ("/delete/{idsStr}")
    public CommonResp deletes (@PathVariable String idsStr) {
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return  resp;
    }
}
