package com.cheng.spider.core.selector;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/4/21
 */
public class Html extends PlainText {

    public Html(String string) {
        super(string);
    }

    public Html(List<String> strings) {
        super(strings);
    }

    @Override
    public Selectable xpath(String xpath) {
        Selector selector = Selectors.xpath(xpath);
        return selectList(selector, strings);
    }

    @Override
    public Selectable links() {
        Selector selector = Selectors.xpath("//a/@href");
        return selectList(selector, strings);
    }

    @Override
    protected Selectable select(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<>();
        for (String string : strings) {
            String result = selector.select(string);
            results.add(result);
        }
        return new Html(results);
    }

    @Override
    protected Selectable selectList(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<>();
        for (String string : strings) {
            List<String> result = selector.selectList(string);
            results.addAll(result);
        }
        return new Html(results);
    }
}
