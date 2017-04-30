package com.cheng.spider.core.scheduler;

import com.cheng.spider.core.Request;
import com.cheng.spider.core.Task;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Desc: 有优先级的; 越小的数越被先执行 支持负数
 * Author: 光灿
 * Date: 2017/4/26
 */
public class PriorityScheduler implements Scheduler {

    private static final int INITIAL_CAPACITY = 5;

    private BlockingQueue<Request> noPriorityQueue = new LinkedBlockingQueue<>();

    private PriorityBlockingQueue<Request> priorityQueuePlus = new PriorityBlockingQueue<>(INITIAL_CAPACITY, new Comparator<Request>() {
        @Override
        public int compare(Request o1, Request o2) {
            return compareLong(o1.getPriority(), o2.getPriority());
        }
    });

    private PriorityBlockingQueue<Request> priorityQueueMinus = new PriorityBlockingQueue<>(INITIAL_CAPACITY, new Comparator<Request>() {
        @Override
        public int compare(Request o1, Request o2) {
            return compareLong(o1.getPriority(), o2.getPriority());
        }
    });

    private Set<String> urls = new HashSet<>();

    @Override
    public void push(Request request, Task task) {
        if (urls.add(request.getUrl())) {
            if (request.getPriority() == 0L) {
                noPriorityQueue.add(request);
            }else if (request.getPriority() > 0L) {
                priorityQueuePlus.put(request);
            }else {
                priorityQueueMinus.put(request);
            }
        }
    }

    @Override
    public Request poll(Task task) {
        Request request = priorityQueueMinus.poll();
        if (request != null) {
            return request;
        }
        request = noPriorityQueue.poll();
        if (request != null) {
            return request;
        }
        return priorityQueuePlus.poll();
    }

    private int compareLong(long o1, long o2) {
        if (o1 > o2) return 1;
        else if (o1 == o2) return 0;
        else return -1;
    }

}
