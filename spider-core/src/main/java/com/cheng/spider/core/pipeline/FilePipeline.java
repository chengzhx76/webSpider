package com.cheng.spider.core.pipeline;

import com.cheng.spider.core.ResultItems;
import com.cheng.spider.core.Task;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Desc: 抽取结果实例化到文件
 * Author: 光灿
 * Date: 2017/4/22
 */
public class FilePipeline implements Pipeline {

    private Logger log = LoggerFactory.getLogger(getClass());

    private String path = "c:\\";

    /**
     * 使用默认路径
     */
    public FilePipeline(){};

    public FilePipeline(String path) {
        this.path = path;
    }

    @Override
    public void process(ResultItems items, Task task) {
        String path = this.path + File.separator + task.getUUID() + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(path + DigestUtils.md5Hex(items.getRequest().getUrl()));
            printWriter.println("url:\t" + items.getRequest().getUrl());
            for (Map.Entry<String, Object> entry : items.getAll().entrySet()) {
                printWriter.println(entry.getKey()+":\t"+entry.getValue());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter !=null )
                printWriter.close();
        }
    }
}
