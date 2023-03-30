package com.example.springbootdemothree.service;

import com.example.springbootdemothree.domain.Ebook;
import com.example.springbootdemothree.domain.EbookExample;
import com.example.springbootdemothree.mapper.EbookMapper;
import com.example.springbootdemothree.req.EbookReq;
import com.example.springbootdemothree.resp.EbookResp;
import com.example.springbootdemothree.resp.PageResp;
import com.example.springbootdemothree.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;


@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    public PageResp<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {

        criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

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
        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setPage(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }
}
