package com.cheng.spider.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/25
 */
public class ResultItems {

    private Map<String, Object> fields = new HashMap<>();

    private Request request;

    private boolean skip;

    public Object get(String key) {
        return fields.get(key);
    }

    public Map<String, Object> getAll() {
        return fields;
    }

    public ResultItems putField(String key, Object value) {
        fields.put(key, value);
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public ResultItems setRequest(Request request) {
        this.request = request;
        return this;
    }

    public boolean isSkip() {
        return skip;
    }

    public ResultItems setSkip(boolean skip) {
        this.skip = skip;
        return this;
    }
}
