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

    /**
     * 是否忽略这个页面，用于pipeline来判断是否对这个页面进行处理
     * @return 是否忽略 true 忽略
     */
    public boolean isSkip() {
        return skip;
    }

    /**
     * 设置是否忽略这个页面，用于pipeline来判断是否对这个页面进行处理
     * @param skip
     * @return this
     */
    public ResultItems setSkip(boolean skip) {
        this.skip = skip;
        return this;
    }
}
