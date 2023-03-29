package com.example.springbootdemothree.service;

import com.example.springbootdemothree.domain.Ebook;
import com.example.springbootdemothree.domain.EbookExample;
import com.example.springbootdemothree.mapper.EbookMapper;
import com.example.springbootdemothree.req.EbookReq;
import com.example.springbootdemothree.resp.EbookResp;
import com.example.springbootdemothree.util.CopyUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        //  List<EbookResp> respList = new ArrayList<>();
        //  for (Ebook ebook : ebookList) {
        //  //  EbookResp ebookResp = new EbookResp();
        //  //  BeanUtils.copyProperties(ebook, ebookResp)
        //  //  对象复制
        //  EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
        //  respList.add(ebookResp);
        //  }

        //列表复制
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);

        return list;
    }
}
