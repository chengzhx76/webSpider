package com.cheng.spider.core.selector;

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
}
