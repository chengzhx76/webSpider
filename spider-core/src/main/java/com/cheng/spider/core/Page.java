package com.cheng.spider.core;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/4
 */
public class Page {

    private Request request;

    private ResultItems items = new ResultItems();

    private List<Request> targetRequest = new ArrayList<>();

    private String html;

    private String url;

    /**
     * 保存抽取的结果
     * @param key
     * @param field
     */
    public void putField(String key, Object field) {
        items.putField(key, field);
    }

    public void addTargetRequest(List<String> requestUrls) {
        for (String url : requestUrls) {
            if (checkLegalUrl(url)) {
                break;
            }

            targetRequest.add(new Request(url));
        }
    }

    public void addTargetRequest(String requestUrl) {
        if (checkLegalUrl(requestUrl)) {
            return;
        }
        targetRequest.add(new Request(requestUrl));
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
        this.items.setRequest(request);
    }

    public ResultItems getItems() {
        return items;
    }

    public List<Request> getTargetRequest() {
        return targetRequest;
    }

    public void setTargetRequest(List<Request> targetRequest) {
        this.targetRequest = targetRequest;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private boolean checkLegalUrl(String addr) {
        return !(Strings.isNullOrEmpty(addr)
                || addr.equals("#")
                || addr.startsWith("javascript:"));
    }
}
