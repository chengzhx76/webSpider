package com.cheng.spider.core.selector;

/**
 * Desc: 实例化抽取器
 * Author: hp
 * Date: 2017/4/19
 */
public class Selectors {

    public static RegexSelector regex(String regexStr) {
        return new RegexSelector(regexStr);
    }

    public static RegexSelector regex(String regexStr, int group) {
        return new RegexSelector(regexStr, group);
    }

}
