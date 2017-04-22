package com.cheng.spider.core.processor;

import com.cheng.spider.core.Page;
import com.cheng.spider.core.Site;
import com.cheng.spider.core.Spider;

import java.util.List;

/**
 * Desc: 新浪博客
 * Author: 光灿
 * Date: 2017/4/22
 */
public class SinaBlogProcesser implements PageProcessor {

    private Site site;

    @Override
    public void process(Page page) {

        List<String> links = page.getHtml().xpath("//div[@class='articalfrontback SG_j_linedot1 clearfix']").links().all();
        System.out.println(links);

        page.addTargetRequest(links);
        page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2"));
        page.putField("id", page.getUrl().regex("http://blog\\.sina\\.com\\.cn/s/blog_(\\w+)"));
    }

    @Override
    public Site getSite() {

        if (site == null) {
            site = Site.me().setDomain("blog.sina.com.cn")
                    .addStartUrls("http://blog.sina.com.cn/s/blog_4701280b0102egl0.html")
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
                    .setSleepTime(3000);
        }
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new SinaBlogProcesser()).run();
    }
}
