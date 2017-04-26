package com.cheng.spider.core.scheduler;

import com.cheng.spider.core.Request;
import com.cheng.spider.core.Task;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Desc: 有优先级的
 * Author: 光灿
 * Date: 2017/4/26
 */
public class PriorityScheduler implements Scheduler {

    private static final int INITIAL_CAPACITY = 5;

    private BlockingQueue noPriorityQueue = new PriorityBlockingQueue();

    private PriorityBlockingQueue<Request> priorityQueuePlus = new PriorityBlockingQueue<Request>(INITIAL_CAPACITY, new Comparator<Request>() {
        @Override
        public int compare(Request o1, Request o2) {
            return compareLong(o1.getPriority(), o2.getPriority());
        }
    });

    private PriorityBlockingQueue<Request> priorityQueueMinus = new PriorityBlockingQueue<Request>(INITIAL_CAPACITY, new Comparator<Request>() {
        @Override
        public int compare(Request o1, Request o2) {
            return compareLong(o1.getPriority(), o2.getPriority());
        }
    });


    @Override
    public void push(Request request, Task task) {

    }

    @Override
    public Request poll(Task task) {
        return null;
    }

    private int compareLong(long o1, long o2) {
        if (o1 > o2) return 1;
        else if (o1 == o2) return 0;
        else return -1;
    }

}
