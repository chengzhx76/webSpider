package com.cheng.spider.core.selector;

import java.util.List;

/**
 * Desc: 抽取器，先实现正则抽取器
 * Author: 光灿
 * Date: 2017/4/9
 */
public interface Selector {
    /**
     * 抽取抓取的内容
     * @param text 抓取的内容
     * @return
     */
    String select(String text);

    /**
     * 抽取抓取的内容
     * @param text 抓取的内容
     * @return
     */
    List<String> selectList(String text);

}
