package com.cheng.spider.core.pipeline;

import com.cheng.spider.core.ResultItems;
import com.cheng.spider.core.Spider;

import java.util.Map;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/26
 */
public class ConsolePipeline implements Pipeline {

    @Override
    public void process(ResultItems items, Spider spider) {
        System.out.println("get page: "+items.getRequest().getUrl());
        for (Map.Entry<String, Object> entry : items.getAll().entrySet()) {
            System.out.println(entry.getKey()+":\t"+entry.getValue());
        }
    }
}
