package com.cheng.spider.core.selector;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Desc: 正则表达式的抽取器
 * Author: hp
 * Date: 2017/4/18
 */
public class RegexSelector implements Selector {

    private String regexStr;

    private Pattern regex;

    public RegexSelector(String regexStr) {
        if (Strings.isNullOrEmpty(regexStr)) {
            throw new IllegalArgumentException("正则表达式不能为空");
        }
        if (!StringUtils.contains(regexStr, "(") || !StringUtils.contains(regexStr, ")")) {
            throw new IllegalArgumentException("正则表达式必须用“()”包裹");
        }
        if (!StringUtils.contains(regexStr, "(") && !StringUtils.contains(regexStr, ")")) {
            this.regexStr = "(" + regexStr + ")";
        }
        try {
            regex = Pattern.compile(regexStr, /*Pattern.DOTALL|*/Pattern.CASE_INSENSITIVE);
        }catch (PatternSyntaxException) {
            throw new IllegalArgumentException("不合法的正则表达式");
        }
    }

    @Override
    public String select(String text) {
        return null;
    }

    @Override
    public List<String> selectList(String text) {
        return null;
    }

    public RegexResult selectGroup(String text) {
        Matcher matcher = regex.matcher(text);
        if (matcher.find()) {
            String[] groups = new String[matcher.groupCount() + 1];
            for (int i = 0; i < groups.length; i++) {
                groups[i] = matcher.group(i);
            }
            return new RegexResult(groups);
        }
        return RegexResult.EMPTY_RESULT;
    }
}
