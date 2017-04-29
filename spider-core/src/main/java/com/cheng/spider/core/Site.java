package com.cheng.spider.core;

import java.util.*;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/25
 */
public class Site {

    private String domain;

    private String userAgent;

    private String charset;

    private List<String> startUrls = new ArrayList<>();

    private Map<String, String> cookies = new HashMap<>();

    private int sleepTime = 3000;

    private int retryTimes = 0;

    private boolean downloadMedia = false;

    private String mediaDirectory;

    private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<>();

    private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;

    static {
        DEFAULT_STATUS_CODE_SET.add(200);
    }

    public static Site me() {
        return new Site();
    }

    public String getDomain() {
        return domain;
    }

    public Site setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public List<String> getStartUrls() {
        return startUrls;
    }

    public Site addStartUrls(String startUrl) {
        this.startUrls.add(startUrl);
        return this;
    }

    public Site addCookie(String name, String value) {
        cookies.put(name, value);
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Site setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public Site isDownloadMedia(boolean downloadMedia) {
        this.downloadMedia = downloadMedia;
        return this;
    }

    public boolean getDownloadMedia() {
        return downloadMedia;
    }

    public Site setMediaDirectory(String mediaDirectory) {
        this.mediaDirectory = mediaDirectory;
        return this;
    }

    public String getMediaDirectory() {
        return mediaDirectory;
    }

    public Set<Integer> getAcceptStatCode() {
        return acceptStatCode;
    }

    public Site setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.acceptStatCode = acceptStatCode;
        return this;
    }
}
