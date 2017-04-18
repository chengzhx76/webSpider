package com.cheng.spider.core.selector;

/**
 * Desc: 正则表达式的抽取结果
 * Author: hp
 * Date: 2017/4/18
 */
public class RegexResult {

    private String[] groups;

    public static final RegexResult EMPTY_RESULT = new RegexResult();

    public RegexResult(){};

    public RegexResult(String[] groups) {
        this.groups = groups;
    }

    public String get(int groupId) {
        if (groups == null) {
            return null;
        }
        return groups[groupId];
    }

}
