package com.example.springbootdemothree.controller;

import com.example.springbootdemothree.req.CategoryQueryReq;
import com.example.springbootdemothree.req.CategorySaveReq;
import com.example.springbootdemothree.req.EbookQueryReq;
import com.example.springbootdemothree.req.EbookSaveReq;
import com.example.springbootdemothree.resp.CategoryQueryResp;
import com.example.springbootdemothree.resp.CommonResp;
import com.example.springbootdemothree.resp.EbookQueryResp;
import com.example.springbootdemothree.resp.PageResp;
import com.example.springbootdemothree.service.CategoryService;
import com.example.springbootdemothree.service.EbookService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp list (@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return  resp;
    }

    @PostMapping ("/save")
    public CommonResp save (@RequestBody @Valid CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return  resp;
    }

    @DeleteMapping ("/delete/{id}")
    public CommonResp delete (@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return  resp;
    }
}
