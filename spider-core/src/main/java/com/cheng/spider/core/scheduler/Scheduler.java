package com.cheng.spider.core.scheduler;

import com.cheng.spider.core.Request;
import com.cheng.spider.core.Task;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/26
 */
public interface Scheduler {
    /**
     * 加入一个待抓取的链接
     * @param request 待抓取的链接
     * @param task 定义的任务，以满足单Scheduler多Task的情况
     */
    void push(Request request, Task task);

    /**
     * 返回下一个要抓取的链接
     * @param task 定义的任务，以满足单Scheduler多Task的情况
     * @return 下一个要抓取的链接
     */
    Request poll(Task task);
}
