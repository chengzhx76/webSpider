package com.cheng.spider.core.downloader;

import com.cheng.spider.core.Page;
import com.cheng.spider.core.Request;
import com.cheng.spider.core.Task;

/**
 * Desc: 下载器
 * Author: 光灿
 * Date: 2017/3/4
 */
public interface Downloader {
    /**
     * 下载
     * @param request
     * @param task
     * @return
     */
    Page download(Request request, Task task);
}
