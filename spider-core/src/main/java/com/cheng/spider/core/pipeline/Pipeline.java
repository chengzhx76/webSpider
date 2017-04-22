package com.cheng.spider.core.pipeline;

import com.cheng.spider.core.ResultItems;
import com.cheng.spider.core.Task;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/25
 */
public interface Pipeline {

    void process(ResultItems items, Task task);
}
