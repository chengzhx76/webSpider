package com.cheng.spider.core;

import com.cheng.spider.core.selector.Selectable;
import com.cheng.spider.core.util.Constant;
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

    public void addTargetMediaRequest(List<String> targetUrls) {
        addTargetMediaSubdirRequest(targetUrls, null);
    }

    public void addTargetMediaSubdirRequest(List<String> targetUrls, String subdires) {
        addTargetMediaSubdirTitleRequest(targetUrls, subdires, null);
    }

    public void addTargetMediaTitleRequest(List<String> targetUrls, String title) {
        addTargetMediaSubdirTitleRequest(targetUrls, null, title);
    }

    public void addTargetMediaSubdirTitleRequest(List<String> targetUrls, String subdires, String title) {
        for (String url : targetUrls) {
            addTargetMediaSubdirTitleRequest(url, subdires, title);
        }
    }

    public void addTargetMediaRequest(String targetUrl) {
        addTargetMediaSubdirRequest(targetUrl, null);
    }

    public void addTargetMediaTitleRequest(String targetUrl, String title) {
        addTargetMediaSubdirTitleRequest(targetUrl, null, title);
    }

    public void addTargetMediaSubdirRequest(String targetUrl, String subdires) {
        addTargetMediaSubdirTitleRequest(targetUrl, subdires, null);
    }

    public void addTargetMediaSubdirTitleRequest(String targetUrl, String subdires, String title) {
        if (!Strings.isNullOrEmpty(targetUrl)) {
            if (!Strings.isNullOrEmpty(subdires) && !Strings.isNullOrEmpty(title)) {
                targetMediaRequest.add(Request.createMediaRequest(targetUrl).setSubdires(subdires).putExtra(Constant.TITLE, title));
            } else if (!Strings.isNullOrEmpty(subdires)) {
                targetMediaRequest.add(Request.createMediaRequest(targetUrl).setSubdires(subdires));
            } else if (!Strings.isNullOrEmpty(title)) {
                targetMediaRequest.add(Request.createMediaRequest(targetUrl).putExtra(Constant.TITLE, title));
            } else {
                targetMediaRequest.add(Request.createMediaRequest(targetUrl));
            }
        }
    }

    public void addTargetRequest(List<String> requestUrls) {
        for (String url : requestUrls) {
            addTargetRequest(url);
        }
    }

    public void addTargetRequest(String requestUrl) {
        if (checkLegalUrl(requestUrl)) {
            return;
        }
        targetRequest.add(new Request(requestUrl, Constant.HTML));
    }

    /**
     * 保存抽取的结果
     * @param key
     * @param field
     */
    public void putField(String key, Object field) {
        items.putField(key, field);
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
