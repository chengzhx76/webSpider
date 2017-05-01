package com.cheng.spider.example;

import com.cheng.spider.core.Page;
import com.cheng.spider.core.Site;
import com.cheng.spider.core.Spider;
import com.cheng.spider.core.pipeline.ConsolePipeline;
import com.cheng.spider.core.processor.PageProcessor;
import com.cheng.spider.core.scheduler.QuenueScheduler;

import java.util.List;

/**
 * Desc: 99mm图片
 * Author: 光灿
 * Date: 2017/4/30
 */
public class NinenineProcessor implements PageProcessor {
    private Site site;

    @Override
    public void process(Page page) {
        List<String> navPage = page.getHtml().xpath("//*[@id='piclist']/li/dl/dt").links().all();
        page.addTargetRequest(navPage);

        List<String> navPageImgs = page.getHtml().xpath("//*[@id='piclist']/li/dl/dt/a/img/@src").all();
        page.addTargetMediaRequest(navPageImgs);

        String detailPageImg = page.getHtml().xpath("//*[@id='picbox']/img/@src").toString();
        String detailPageTitle = page.getHtml().xpath("//div[@class='title']/span/h2").toString();
        page.addTargetMediaTitleRequest(detailPageImg, detailPageTitle);

        String detailPageNumber = page.getHtml().xpath("//*[@id='page']/a[last()]/@href")
                .regex("http://www.99mm.me/([a-z]+\\/\\w+\\.html\\?url=[1-9]+)").toString();
        page.addTargetRequest(detailPageNumber);
    }

    @Override
    public Site getSite() {
        if (site == null) {
            site = Site.me()
                    .addStartUrls("http://www.99mm.me/")
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
                    .isDownloadMedia(true)
                    .setMediaDirectory("C:\\QQDownload\\99mm\\")
                    //.setMediaDirectory("/root/downloads/")
                    .setSleepTime(1000);
        }
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new NinenineProcessor())
                .pipeline(new ConsolePipeline())
                .scheduler(new QuenueScheduler())
                .run();
    }
}
