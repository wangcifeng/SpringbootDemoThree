package com.example.springbootdemothree.resp;

import java.util.List;

public class PageResp<T> {
    private Long page;

    private List<T> list;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResp{" +
                "page=" + page +
                ", list=" + list +
                '}';
    }
}