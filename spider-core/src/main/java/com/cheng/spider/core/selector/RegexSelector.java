package com.cheng.spider.core.selector;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
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

    public RegexSelector(){}

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
        }catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("不合法的正则表达式");
        }
    }

    @Override
    public String select(String text) {
        return selectGroup(text).get(1);
    }

    @Override
    public List<String> selectList(String text) {
        List<String> results = new ArrayList<>();
        List<RegexResult> regexResults = selectGroupList(text);
        for (RegexResult regexResult : regexResults) {
            results.add(regexResult.get(1));
        }
        return results;
    }

    private RegexResult selectGroup(String text) {
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

    private List<RegexResult> selectGroupList(String text) {
        List<RegexResult> results = new ArrayList<>();
        Matcher matcher = regex.matcher(text);

        while (matcher.find()) {
            String[] groups = new String[matcher.groupCount() + 1];
            for (int i = 0; i < groups.length; i++) {
                groups[i] = matcher.group(i);
            }
            results.add(new RegexResult(groups));
        }
        return results;
    }

    @Override
    public String toString() {
        return regexStr;
    }
}
