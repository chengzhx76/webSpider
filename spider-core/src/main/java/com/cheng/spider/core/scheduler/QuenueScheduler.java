package com.cheng.spider.core.scheduler;

import com.cheng.spider.core.Request;
import com.cheng.spider.core.Task;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/26
 */
public class QuenueScheduler implements Scheduler {

    private BlockingQueue<Request> queue = new LinkedBlockingDeque<>();

    private Set<String> urls = new HashSet<>();

    @Override
    public synchronized void push(Request request, Task task) {
        if (urls.add(request.getUrl())) {
            queue.add(request);
        }
    }

    @Override
    public synchronized Request poll(Task task) {
        return queue.poll();
    }
}
