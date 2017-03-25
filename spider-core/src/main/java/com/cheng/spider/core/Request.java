package com.cheng.spider.core;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/25
 */
public class Request {

    private String url;

    private Object[] extra;

    public Request(String url, Object... extra) {
        this.url = url;
        this.extra = extra;
    }

    public String getUrl() {
        return url;
    }

    public Object[] getExtra() {
        return extra;
    }
}
