package com.example.springbootdemothree.service;

import com.example.springbootdemothree.domain.Ebook;
import com.example.springbootdemothree.domain.EbookExample;
import com.example.springbootdemothree.mapper.EbookMapper;
import com.example.springbootdemothree.req.EbookReq;
import com.example.springbootdemothree.resp.EbookResp;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list (EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" +req.getName() + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> respList = new ArrayList<>();

        for (Ebook ebook : ebookList) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook, ebookResp);
            //ebookResp.setId(123L);
            respList.add(ebookResp);
        }

        return respList;
    }
}
