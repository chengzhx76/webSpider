package com.cheng.spider.core;

import com.cheng.spider.core.downloader.Downloader;
import com.cheng.spider.core.downloader.HttpClientDownloader;
import com.cheng.spider.core.downloader.MediaDownloader;
import com.cheng.spider.core.pipeline.ConsolePipeline;
import com.cheng.spider.core.pipeline.Pipeline;
import com.cheng.spider.core.processor.PageProcessor;
import com.cheng.spider.core.scheduler.QuenueScheduler;
import com.cheng.spider.core.scheduler.Scheduler;
import com.cheng.spider.core.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/25
 */
public class Spider implements Runnable, Task {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Site site;

    private String uuid;

    private List<String> startUrls;

    private Downloader downloader = new HttpClientDownloader();

    private Downloader mediaDownloader = new MediaDownloader();

    private List<Pipeline> pipelines = new ArrayList<>();

    private Scheduler scheduler = new QuenueScheduler();

    private Scheduler mediaScheduler = new QuenueScheduler();

    private PageProcessor processor;

    private ExecutorService executorService;

    private AtomicInteger state = new AtomicInteger(STATE_INIT);

    private final static int STATE_INIT = 0;

    private final static int STATE_RUNNING = 1;

    private final static int STATE_STOPPED = 2;

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
        checkIfNotRunning();
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
        checkIfNotRunning();
        this.scheduler = scheduler;
        return this;
    }

    /**
     * 设置处理管道。处理管道用于最终抽取结果的后处理，例如：保存到文件、保存到数据库等。默认情况下会输出到控制台。
     * @param pipeline 处理管道
     * @return this
     */
    public Spider pipeline(Pipeline pipeline) {
        checkIfNotRunning();
        this.pipelines.add(pipeline);
        return this;
    }

    /**
     * 设置下载器。处理下载页面的请求。默认情况下使用HttpClient。
     * @param downloader 下载器
     * @return this
     */
    public Spider downloader(Downloader downloader) {
        checkIfNotRunning();
        this.downloader = downloader;
        return this;
    }

    /**
     * 异步抓取
     */
    public void runAsync() {
        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }

    /**
     * 设置线程数
     * @param threadNum
     * @return
     */
    public Spider thread(int threadNum) {
        checkIfNotRunning();
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        if (threadNum == 1) {
            return this;
        }
        synchronized (this) {
            executorService = Executors.newFixedThreadPool(threadNum);
        }
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

    private AtomicInteger htmlState = new AtomicInteger(STATE_INIT);
    @Override
    public void run() {

        if (!state.compareAndSet(STATE_INIT, STATE_RUNNING)) {
            throw new IllegalStateException("Spider is already running!");
        }

        if (startUrls != null) {
            for (String startUrl : startUrls) {
                scheduler.push(new Request(startUrl, Constant.HTML), this);
            }
        }

        if (pipelines.isEmpty()) {
            pipelines.add(new ConsolePipeline());
        }

        // 网页内容下载
        Request request = scheduler.poll(this);
        new Thread(new DownloadContent(request, this), "HTMLDownload").start();

        // 图片下载
        if (isDownloadMedia()) {
            Request mediaRequest = mediaScheduler.poll(this);
            new Thread(new DownloadMedia(mediaRequest, this), "MediaDownload").start();
        }
    }

    class DownloadContent implements Runnable {
        private Request request;
        private Task task;

        public DownloadContent(Request request, Task task) {
            this.request = request;
            this.task = task;
        }

        @Override
        public void run() {
            log.info("下载HTML--Start");
            if (!htmlState.compareAndSet(STATE_INIT, STATE_RUNNING)) {
                throw new IllegalStateException("htmlDownload is already running!");
            }
            if (executorService == null) { // 单线程
                while (request != null) {
                    processRequest(request);
                    request = scheduler.poll(task);
                }
            } else { // 多线程
                final AtomicInteger threadAlive = new AtomicInteger(0);
                while (true) {
                    if (request == null) {
                        sleep(site.getSleepTime());
                    } else {
                        final Request finalRequest = request;
                        threadAlive.getAndIncrement();
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                processRequest(finalRequest);
                                threadAlive.decrementAndGet();
                            }
                        });
                    }

                    request = scheduler.poll(task);
                    if (threadAlive.get() == 0) {
                        request = scheduler.poll(task);
                        if (request == null) {
                            break;
                        }
                    }
                }
                executorService.shutdown();
            }
            htmlState.compareAndSet(STATE_RUNNING, STATE_STOPPED);
            log.info("下载HTML--End");
        }
    }

    class DownloadMedia implements Runnable {
        private Request mediaRequest;
        private Task task;

        public DownloadMedia(Request mediaRequest, Task task) {
            this.mediaRequest = mediaRequest;
            this.task = task;
        }

        @Override
        public void run() {
            log.info("下载IMG--Start");
            while (true) {
                if (mediaRequest == null) {
                    sleep(site.getSleepTime());
                } else {
                    processMediaRequest(mediaRequest);
                }
                mediaRequest = mediaScheduler.poll(task);

                if (htmlState.get() == STATE_STOPPED) {
                    if (mediaRequest == null) {
                        break;
                    }
                }
            }
            log.info("下载IMG--End");

            state.compareAndSet(STATE_RUNNING, STATE_STOPPED);
        }
    }

    /**
     * 处理网页文件请求
     * @param request
     */
    private void processRequest(Request request) {
        Page page = downloader.download(request, this);
        if (page == null) {
            sleep(site.getSleepTime());
            return;
        }
        processor.process(page);
        addRequest(page);
        handlePipeline(page);
        sleep(site.getSleepTime());
    }

    /**
     * 处理资源文件请求
     * @param request
     */
    private void processMediaRequest(Request request) {
        Page page = mediaDownloader.download(request, this);
        if (page == null) {
            sleep(site.getSleepTime());
            return;
        }
        handlePipeline(page);
        sleep(site.getSleepTime());
    }

    /**
     * 操作持久化
     * @param page
     */
    private synchronized void handlePipeline(Page page) {
        if (!page.getItems().isSkip()) {
            for (Pipeline pipeline : pipelines) {
                pipeline.process(page.getItems(), this);
            }
        }
    }

    /**
     * 解析后获取URL
     * @param page
     */
    private void addRequest(Page page) {
        // 处理网页内容
        if (page.getTargetRequest() != null && !page.getTargetRequest().isEmpty()) {
            for (Request request : page.getTargetRequest()) {
                scheduler.push(request, this);
            }
        }

        // 处理图片URL
        if (isDownloadMedia()) {
            if (page.getTargetMediaRequest() != null && !page.getTargetMediaRequest().isEmpty()) {
                for (Request request : page.getTargetMediaRequest()) {
                    mediaScheduler.push(request, this);
                }
            }
        }
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否还在运行
     */
    private void checkIfNotRunning() {
        if (state.get() == STATE_RUNNING) {
            throw new IllegalStateException("爬虫已经在运行！");
        }
    }

    /**
     * 是否下载媒体资源
     * @return
     */
    private boolean isDownloadMedia() {
        return site.getDownloadMedia();
    }

}
