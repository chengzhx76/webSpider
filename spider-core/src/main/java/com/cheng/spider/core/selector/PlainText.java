package com.cheng.spider.core.selector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Desc: 文本抽取器
 * Author: hp
 * Date: 2017/4/20
 */
public class PlainText implements Selectable {

    protected List<String> strings = new ArrayList<>();

    public PlainText(String string) {
        strings.add(string);
    }

    public PlainText(List<String> string) {
        this.strings = string;
    }

    @Override
    public Selectable regex(String regex) {
        RegexSelector selector = Selectors.regex(regex);
        return selectList(selector, strings);
    }

    @Override
    public List<String> all() {
        //return strings;
        return new ArrayList<>(new HashSet<>(strings));
    }

    @Override
    public String toString() {
        if (strings != null && !strings.isEmpty()) {
            return strings.get(0);
        }
        return null;
    }

    @Override
    public Selectable xpath(String xpath) {
        throw new UnsupportedOperationException("没实现");
    }

    @Override
    public Selectable links() {
        throw new UnsupportedOperationException("没实现");
    }

    protected Selectable select(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<>();
        for (String regex : strings) {
            String result = selector.select(regex);
            if (result != null) {
                results.add(result);
            }
        }
        return new PlainText(results);
    }

    protected Selectable selectList(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<>();
        for (String string : strings) {
            List<String> result = selector.selectList(string);
            if (result != null) {
                results.addAll(result);
            }
        }
        return new PlainText(results);
    }

}
