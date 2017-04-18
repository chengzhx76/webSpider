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
        //String text = "1458727543----WQDPVCTBUW-1441794418CTBUW-1441794419";
        //String regex = "[1-9][0-9]{4,}";
        //String regex = "([1-9][0-9]{4,})";
        String text = "http://blog.sina.com.cn/s/blog_4701280b0102egl0.html";
        String regex = "http://blog.sina.com.cn/(s)/blog_(\\w+)";

        RegexSelector selector = new RegexSelector(regex, 1);
        //RegexSelector selector = new RegexSelector(regex, 2);
        String result = selector.select(text);
        List<String> results = selector.selectList(text);

        System.out.println(result);
        System.out.println(results);
    }

}
