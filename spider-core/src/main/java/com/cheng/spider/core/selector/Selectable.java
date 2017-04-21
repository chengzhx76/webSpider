package com.cheng.spider.core.selector;

import java.util.List;

/**
 * Desc: 可进行抽取的文本
 * Author: hp
 * Date: 2017/4/20
 */
public interface Selectable {
    /**
     * 正则抽取
     * @param regex
     * @return
     */
    Selectable regex(String regex);

    /**
     * 抽取的所有结果
     * @return
     */
    List<String> all();

    /**
     * 抽取结果
     * @return
     */
    String toString();
}
