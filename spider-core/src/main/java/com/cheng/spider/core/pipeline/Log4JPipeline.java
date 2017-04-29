package com.cheng.spider.core.pipeline;

import com.cheng.spider.core.ResultItems;
import com.cheng.spider.core.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/4/29
 */
public class Log4JPipeline implements Pipeline {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void process(ResultItems items, Task task) {
        log.info("------------------------------------------------");
        log.info("get "+ items.getRequest().getType() +": "+items.getRequest().getUrl());
        for (Map.Entry<String, Object> entry : items.getAll().entrySet()) {
            log.info(entry.getKey()+"："+entry.getValue());
        }
    }
}
