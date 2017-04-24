package com.cheng.spider.core;

import com.cheng.spider.core.downloader.FileDownloader;
import com.cheng.spider.core.pipeline.ConsolePipeline;
import com.cheng.spider.core.pipeline.Pipeline;
import com.cheng.spider.core.scheduler.Scheduler;
import com.cheng.spider.core.downloader.Downloader;
import com.cheng.spider.core.downloader.HttpClientDownloader;
import com.cheng.spider.core.processor.PageProcessor;
import com.cheng.spider.core.scheduler.QuenueScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/25
 */
public class Spider implements Runnable, Task {

    private Site site;

    private String uuid;

    private List<String> startUrls;

    private Downloader downloader = new HttpClientDownloader();

    private Downloader imgDownloader = new FileDownloader();

    private List<Pipeline> pipelines = new ArrayList<>();

    private Scheduler scheduler = new QuenueScheduler();

    private Scheduler imgScheduler = new QuenueScheduler();

    private PageProcessor processor;

    /**
     * 使用已定义抽取规则新建一个爬虫
     * @param processor 已自定抽取义规则
     */
    public Spider(PageProcessor processor) {
        this.processor = processor;
        this.site = processor.getSite();
        this.startUrls = processor.getSite().getStartUrls();
    }
    /**
     * 使用已定义抽取规则新建一个爬虫
     * @param processor 已自定抽取义规则
     * @return 新建的爬虫
     */
    public static Spider create(PageProcessor processor) {
        return new Spider(processor);
    }

    /**
     * 重新设置startUrls，会覆盖Site本身的startUrls
     * @param startUrls 开始爬取的地址
     * @return 新建的爬虫
     */
    public Spider startUrls(List<String> startUrls) {
        this.startUrls = startUrls;
        return this;
    }

    /**
     * 为爬虫设置一个唯一ID，用于标志任务，默认情况下使用domain作为uuid，对于单domain多任务的情况，请为重复任务设置不同的ID。
     * @param uuid 唯一ID
     * @return this
     */
    public Spider setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    /**
     * 设置调度器。调度器用于保存待抓取URL，并可以进行去重、同步、持久化等工作。默认情况下使用内存中的阻塞队列进行调度。
     * @param scheduler 调度器
     * @return this
     */
    public Spider scheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    /**
     * 设置处理管道。处理管道用于最终抽取结果的后处理，例如：保存到文件、保存到数据库等。默认情况下会输出到控制台。
     * @param pipeline 处理管道
     * @return this
     */
    public Spider pipeline(Pipeline pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }

    /**
     * 设置下载器。处理下载页面的请求。默认情况下使用HttpClient。
     * @param downloader 下载器
     * @return this
     */
    public Spider downloader(Downloader downloader) {
        this.downloader = downloader;
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

    @Override
    public void run() {
        if (startUrls != null) {
            for (String startUrl : startUrls) {
                scheduler.push(new Request(startUrl), this);
            }
        }

        if (pipelines.isEmpty()) {
            pipelines.add(new ConsolePipeline());
        }

        // --------------单线程-----------------
        // 网页内容下载
        Request request = scheduler.poll(this);
        while (request != null) {
            processRequest(request);
            request = scheduler.poll(this);
        }

        // 图片下载
        Request imgRequest = imgScheduler.poll(this);
        while (imgRequest != null) {
            processImgRequest(imgRequest);
            imgRequest = imgScheduler.poll(this);
        }
    }

    private void processRequest(Request request) {
        Page page = downloader.download(request, this);
        if (page == null) {
            sleep(site.getSleepTime());
            return;
        }
        processor.process(page);
        addRequest(page);
        for (Pipeline pipeline : pipelines) {
            pipeline.process(page.getItems(), this);
        }
        sleep(site.getSleepTime());
    }

    private void processImgRequest(Request request) {
        Page page = imgDownloader.download(request, this);
        if (page == null) {
            sleep(site.getSleepTime());
            return;
        }
        for (Pipeline pipeline : pipelines) {
            pipeline.process(page.getItems(), this);
        }
        sleep(site.getSleepTime());
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addRequest(Page page) {
        // 处理网页内容
        if (page.getTargetRequest() != null && !page.getTargetRequest().isEmpty()) {
            for (Request request : page.getTargetRequest()) {
                scheduler.push(request, this);
            }
        }

        // 处理图片URL
        if (page.getTargetImgRequest() != null && !page.getTargetImgRequest().isEmpty()) {
            for (Request request : page.getTargetImgRequest()) {
                imgScheduler.push(request, this);
            }
        }
    }

}
