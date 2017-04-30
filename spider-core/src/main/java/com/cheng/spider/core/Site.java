package com.cheng.spider.core;

import com.cheng.spider.core.util.UrlUtils;

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

    /**
     * 创建一个Site对象，等价于new Site()
     *
     * @return 新建的对象
     */
    public static Site me() {
        return new Site();
    }

    /**
     * 获取已设置的Domain
     * @return
     */
    public String getDomain() {
        if (domain == null) {
            if (startUrls.size() >0) {
                domain = UrlUtils.getDomain(startUrls.get(0));
            }
        }
        return domain;
    }
    /**
     * 设置这个站点所在域名。<br>
     * 目前不支持多个域名的抓取。抓取多个域名请新建一个Spider。
     *
     * @param domain 爬虫会抓取的域名
     * @return this
     */
    public Site setDomain(String domain) {
        this.domain = domain;
        return this;
    }
    /**
     * 获取已设置的user-agent
     *
     * @return 已设置的user-agent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 为这个站点设置user-agent，很多网站都对user-agent进行了限制，不设置此选项可能会得到期望之外的结果。
     *
     * @param userAgent userAgent
     * @return this
     */
    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * 获取已设置的编码
     *
     * @return 已设置的domain
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 设置页面编码，若不设置则自动根据Html meta信息获取。<br>
     * 一般无需设置encoding，如果发现下载的结果是乱码，则可以设置此项。<br>
     *
     * @param charset 编码格式，主要是"utf-8"、"gbk"两种
     * @return this
     */
    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 获取初始页面的地址列表
     *
     * @return 初始页面的地址列表
     */
    public List<String> getStartUrls() {
        return startUrls;
    }

    /**
     * 增加初始页面的地址，可反复调用此方法增加多个初始地址。
     *
     * @param startUrl 初始页面的地址
     * @return this
     */
    public Site addStartUrls(String startUrl) {
        this.startUrls.add(startUrl);
        return this;
    }

    /**
     * 为这个站点添加一个cookie，可用于抓取某些需要登录访问的站点。这个cookie的域名与{@link #getDomain()}是一致的
     *
     * @param name  cookie的名称
     * @param value cookie的值
     * @return this
     */
    public Site addCookie(String name, String value) {
        cookies.put(name, value);
        return this;
    }
    /**
     * 获取已经设置的所有cookie
     *
     * @return 已经设置的所有cookie
     */
    public Map<String, String> getCookies() {
        return cookies;
    }

    /**
     * 获取两次抓取之间的间隔
     *
     * @return 两次抓取之间的间隔，单位毫秒
     */
    public int getSleepTime() {
        return sleepTime;
    }


    /**
     * 设置两次抓取之间的间隔，避免对目标站点压力过大(或者避免被防火墙屏蔽...)。
     *
     * @param sleepTime 单位毫秒
     * @return this
     */
    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    /**
     * 获取重新下载的次数，默认为0
     *
     * @return 重新下载的次数
     */
    public int getRetryTimes() {
        return retryTimes;
    }

    /**
     * 设置获取重新下载的次数，默认为0
     *
     * @return this
     */
    public Site setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    /**
     * 是否下载资源文件
     * @param downloadMedia true or false
     * @return
     */
    public Site isDownloadMedia(boolean downloadMedia) {
        this.downloadMedia = downloadMedia;
        return this;
    }

    /**
     * 获取是否下载资源文件
     * @return
     */
    public boolean getDownloadMedia() {
        return downloadMedia;
    }

    /**
     * 设置下载的资源文件路径
     * @param mediaDirectory
     * @return
     */
    public Site setMediaDirectory(String mediaDirectory) {
        this.mediaDirectory = mediaDirectory;
        return this;
    }

    /**
     * 获取下载资源路径
     * @return
     */
    public String getMediaDirectory() {
        return mediaDirectory;
    }

    /**
     * 获取可接受的状态码
     *
     * @return 可接受的状态码
     */
    public Set<Integer> getAcceptStatCode() {
        return acceptStatCode;
    }

    /**
     * 设置可接受的http状态码，仅当状态码在这个集合中时，才会读取页面内容。<br>
     * 默认为200，正常情况下，无须设置此项。<br>
     * 某些站点会错误的返回状态码，此时可以对这个选项进行设置。<br>
     *
     * @param acceptStatCode 可接受的状态码
     * @return this
     */
    public Site setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.acceptStatCode = acceptStatCode;
        return this;
    }
}
