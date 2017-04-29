package com.cheng.spider.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/25
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 2062192774891352043L;

    private String url;;

    private String subdires;

    private Map<String, Object> extra;

    private long priority;

    public Request(String url) {
        this.url = url;
    }

    public Request(String url, long priority) {
        this.url = url;
        this.priority = priority;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public Object getExtra(String key) {
        if (extra == null) {
            return null;
        }
        return extra.get(key);
    }

    public Request putExtra(String key, Object value) {
        if (extra == null) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
        return this;
    }

    public long getPriority() {
        return priority;
    }

    public Request setSubdires(String subdires) {
        this.subdires = subdires;
        return this;
    }

    public String getSubdires() {
        return subdires;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Request{");
        sb.append("url='").append(url).append('\'');
        sb.append(", subdires='").append(subdires).append('\'');
        sb.append(", extra=").append(extra);
        sb.append(", priority=").append(priority);
        sb.append('}');
        return sb.toString();
    }
}
