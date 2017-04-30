package com.cheng.test.spider;

import com.cheng.spider.core.Site;
import com.cheng.spider.core.Task;
import com.cheng.spider.core.scheduler.PriorityScheduler;
import org.junit.Test;

/**
 * Desc: 优先级列队测试
 * Author: 光灿
 * Date: 2017/4/27
 */
public class PrioritySchedulerTest {

    private PriorityScheduler scheduler = new PriorityScheduler();

    private Task task = new Task() {
        @Override
        public Site getSite() {
            return null;
        }

        @Override
        public String getUUID() {
            return "1";
        }
    };

    @Test
    public void testDifferentPriority() {
        //Request request = new Request("a", 100L);
        //scheduler.push(request, task);
        //
        //request = new Request("b", 900L);
        //scheduler.push(request, task);
        //
        //request = new Request("c", -900L);
        //scheduler.push(request, task);

        //scheduler.push(new Request("a", -5L), task);
        //scheduler.push(new Request("a-1", -7L), task);
        //scheduler.push(new Request("b", 0L), task);
        //scheduler.push(new Request("c", 2L), task);
        //scheduler.push(new Request("c-1", 5L), task);
        //
        //System.out.println(scheduler.poll(task).getUrl());
        //System.out.println(scheduler.poll(task).getUrl());
        //System.out.println(scheduler.poll(task).getUrl());
        //System.out.println(scheduler.poll(task).getUrl());
        //System.out.println(scheduler.poll(task).getUrl());
    }

}
