package com.cheng.spider.core;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/25
 */
public class Spider implements Task {

    private Site site;

    private String uuid;

    /**
     * 为爬虫设置一个唯一ID，用于标志任务，默认情况下使用domain作为uuid，对于单domain多任务的情况，请为重复任务设置不同的ID。
     * @param uuid 唯一ID
     * @return this
     */
    public Spider setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public String getUUID() {
        if (uuid != null) {
            return uuid;
        }
        if (site != null) {
            return site.getDomain();
        }
        return null;
    }

}
