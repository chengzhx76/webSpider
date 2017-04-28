package com.cheng.spider.core;

import com.cheng.spider.core.selector.Selectable;
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

    private List<Request> targetMediaRequest = new ArrayList<>();

    private Selectable html;

    private Selectable url;

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

    public void addTargetMediaRequest(List<String> targetMediaUrls, String title) {
        for (String url : targetMediaUrls) {
            targetMediaRequest.add(new Request(url).setSubdires(title));
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

    public List<Request> getTargetMediaRequest() {
        return targetMediaRequest;
    }

    public void setTargetRequest(List<Request> targetRequest) {
        this.targetRequest = targetRequest;
    }

    public Selectable getHtml() {
        return html;
    }

    public void setHtml(Selectable html) {
        this.html = html;
    }

    public Selectable getUrl() {
        return url;
    }

    public void setUrl(Selectable url) {
        this.url = url;
    }

    private boolean checkLegalUrl(String addr) {
        return Strings.isNullOrEmpty(addr)
                || addr.equals("#")
                || addr.startsWith("javascript:");
    }
}
