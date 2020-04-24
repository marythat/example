package com.newsupplytech.projectapplication.comm.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class DefaultPage<T> extends Page<T> {

    public DefaultPage() {
        super(1,15);
    }

    public DefaultPage(Integer pageNo, Integer pageSize) {
        super(pageNo,pageSize);
    }
}
