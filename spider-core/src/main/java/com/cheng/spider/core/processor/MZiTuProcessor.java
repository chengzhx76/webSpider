package com.cheng.spider.core.processor;

import com.cheng.spider.core.Page;
import com.cheng.spider.core.Site;
import com.cheng.spider.core.Spider;
import com.cheng.spider.core.pipeline.Log4JPipeline;

import java.util.List;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/4/22
 */
public class MZiTuProcessor implements PageProcessor {

    private Site site;

    @Override
    public void process(Page page) {
        // 导航页
        List<String> links = page.getHtml().xpath("//*[@id='pins']").links().all();
        page.addTargetRequest(links);

        // 导航页图
        List<String> imgs = page.getHtml().xpath("//*[@id='pins']/li/a/img/@data-original").all();
        page.addTargetMediaRequest(imgs);

        // 详情页翻页
        List<String> detailPages = page.getHtml().xpath("//div[@class='pagenavi']").links()
                .regex("http:\\/\\/www\\.mzitu\\.com\\/\\d+\\/\\d+").all();
        page.addTargetRequest(detailPages);
        // 详情页大图
        String mainImg = page.getHtml().xpath("//div[@class='main-image']/p/a/img/@src").toString();
        //String path = page.getHtml().xpath("//div[@class='main-image']/p/a/@href").regex("http://www.mzitu.com/(\\w+)").toString();
        String title = page.getHtml().xpath("//div[@class='content']/h2").toString();
        page.addTargetMediaTitleRequest(mainImg, title);

        // http://www.mzitu.com/91263
        page.putField("id", page.getUrl().regex("http://www.mzitu.com/(\\w+)"));
        //page.putField("title", page.getHtml().xpath("//div[@class='content']/h2"));
        //page.putField("主图", page.getHtml().xpath("//div[@class='main-image']/p/a/img/@src"));
    }

    @Override
    public Site getSite() {
        if (site == null) {
            site = Site.me()
                    .setDomain("www.mzitu.com")
                    .addStartUrls("http://www.mzitu.com/")
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
                    .isDownloadMedia(true)
                    //.setDirectory("C:\\QQDownload\\mzt\\")
                    .setDirectory("/root/downloads/")
                    .setSleepTime(1000);
        }
        return site;
    }


    public static void main(String[] args) {
        Spider.create(new MZiTuProcessor()).pipeline(new Log4JPipeline()).run();
    }
}
