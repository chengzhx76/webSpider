package com.cheng.spider.example;

import com.cheng.spider.core.Page;
import com.cheng.spider.core.Site;
import com.cheng.spider.core.Spider;
import com.cheng.spider.core.pipeline.Log4JPipeline;
import com.cheng.spider.core.processor.PageProcessor;

/**
 * Desc:
 * Author: hp
 * Date: 2017/6/6
 */
public class ChainOssProcessor implements PageProcessor {

    private Site site;

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        if (site == null) {
            site = Site.me()
                    .setDomain("trustsql.qq.com")
                    .addStartUrls("https://trustsql.qq.com/chain_oss/index_kx.html")
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
                    .setSleepTime(1000);
        }
        return site;
    }


    public static void main(String[] args) {
        Spider.create(new ChainOssProcessor())
                .pipeline(new Log4JPipeline())
                .run();
    }
}
