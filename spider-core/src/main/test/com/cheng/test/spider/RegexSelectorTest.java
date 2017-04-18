package com.cheng.test.spider;

import com.cheng.spider.core.selector.RegexSelector;
import org.junit.Test;

import java.util.List;

/**
 * Desc: 正则表达式抽取器的测试
 * Author: hp
 * Date: 2017/4/18
 */
public class RegexSelectorTest {

    @Test
    public void test01() {
        String text = "Hello World!";
        String regex = "W(or)(ld!)";

        RegexSelector selector = new RegexSelector(regex);
        String result = selector.select(text);
        List<String> results = selector.selectList(text);

        System.out.println(result);
        System.out.println(results);
    }

}
