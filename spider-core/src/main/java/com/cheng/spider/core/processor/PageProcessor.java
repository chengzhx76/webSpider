package com.cheng.spider.core.processor;

import com.cheng.spider.core.Page;
import com.cheng.spider.core.Site;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/26
 */
public interface PageProcessor {
    /**
     * 定义处理页面的规则
     * @param page
     */
    void process(Page page);

    /**
     * 定义站点信息
     * @return
     */
    Site getSite();
}
