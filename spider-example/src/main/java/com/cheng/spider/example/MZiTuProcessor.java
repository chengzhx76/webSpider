package com.cheng.spider.example;

import com.cheng.spider.core.Page;
import com.cheng.spider.core.Site;
import com.cheng.spider.core.Spider;
import com.cheng.spider.core.pipeline.Log4JPipeline;
import com.cheng.spider.core.processor.PageProcessor;
import com.cheng.spider.core.scheduler.PriorityScheduler;

import java.util.List;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/4/22
 */
public class MZiTuProcessor implements PageProcessor {

    private Site site;

    private long priority = 1L;

    private String pageNums = "1";

    @Override
    public void process(Page page) {
        // 导航页
        List<String> navPage = page.getHtml().xpath("//*[@id='pins']").links().all();
        //page.addTargetRequest(navPage);
        page.addTargetPriorityRequest(navPage, priority);

        // 导航页翻页 -- 只获取下一页的链接
        String navPageNumber = page.getHtml().xpath("//a[@class='next page-numbers']/@href")
                .regex("http:\\/\\/www\\.mzitu\\.com\\/page/\\d+").toString();
        //page.addTargetRequest(navPageNumber);
        page.addTargetPriorityRequest(navPageNumber, priority+2);

        // 导航页图
        List<String> navPageImgs = page.getHtml().xpath("//*[@id='pins']/li/a/img/@data-original").all();
        page.addTargetMediaRequest(navPageImgs);

        // 详情页翻页 -- 只获取下一页的链接
        String detailPageNumber = page.getHtml().xpath("//div[@class='pagenavi']/a[last()]/@href")
                .regex("http:\\/\\/www\\.mzitu\\.com\\/\\d+\\/\\d+").toString();
        //page.addTargetRequest(detailPageNumber);
        page.addTargetPriorityRequest(detailPageNumber, priority-2);

        if (navPageNumber != null && !navPageNumber.isEmpty()) {
            priority +=3;
        }
        //System.out.println(priority);

        // 详情页大图
        String detailPageImg = page.getHtml().xpath("//div[@class='main-image']/p/a/img/@src").toString();
        String detailPagePath = page.getHtml().xpath("//div[@class='main-image']/p/a/@href")
                .regex("http://www.mzitu.com/(\\w+)").toString();
        String detailPageTitle = page.getHtml().xpath("//div[@class='content']/h2").toString();
        //page.addTargetMediaTitleRequest(detailPageImg, detailPageTitle);
        page.addTargetMediaSubdirTitleRequest(detailPageImg, pageNums+"_"+detailPagePath, detailPageTitle);

        // http://www.mzitu.com/91263
        page.putField("id", page.getUrl().regex("http://www.mzitu.com/(\\w+)"));
        String pageNum = page.getUrl().regex("http://www.mzitu.com/page/(\\d+)").toString();
        if (pageNum != null) {
            this.pageNums = pageNum;
        }
        page.putField("page", pageNums);
        page.putField("pageNum", priority/3);
    }

    @Override
    public Site getSite() {
        if (site == null) {
            site = Site.me()
                    .setDomain("www.mzitu.com")
                    .addStartUrls("http://www.mzitu.com")
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
                    .isDownloadMedia(true)
                    //.setMediaDirectory("C:\\QQDownload\\mzt2\\")
                    .setMediaDirectory("/root/downloads/")
                    .setSleepTime(1000);
        }
        return site;
    }


    public static void main(String[] args) {
        Spider.create(new MZiTuProcessor())
                .pipeline(new Log4JPipeline())
                .scheduler(new PriorityScheduler())
                .run();
    }
}
