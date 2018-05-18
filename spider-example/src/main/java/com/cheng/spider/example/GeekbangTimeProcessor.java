package com.cheng.spider.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cheng.spider.core.Page;
import com.cheng.spider.core.Site;
import com.cheng.spider.core.Spider;
import com.cheng.spider.core.pipeline.Log4JPipeline;
import com.cheng.spider.core.processor.PageProcessor;
import com.cheng.spider.core.scheduler.PriorityScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author: hp
 * @date: 2018/5/18
 */
public class GeekbangTimeProcessor implements PageProcessor {

    private Site site;

    private long priority = 1L;

    @Override
    public void process(Page page) {
        String result = page.getHtml().toString();
        if (!result.startsWith("#")) {
            JSONObject resultJson = JSON.parseObject(result);

            // 1.先获取每章视频列表
            JSONArray sectionList = resultJson.getJSONArray("list");
            if (sectionList != null && !sectionList.isEmpty()) {
                List<String> urls = new ArrayList<>();
                for (int i = 0; i < sectionList.size(); i++) {
                    JSONObject section = sectionList.getJSONObject(i);
                    // 2.解释资源
                    String videoMediaJson = section.getString("video_media");
                    JSONObject videoMediaObj = JSON.parseObject(videoMediaJson);
                    // 3.获取当前章节的分片视屏
                    JSONObject hd = videoMediaObj.getJSONObject("hd");
                    urls.add(hd.getString("url"));
                }
                page.addTargetPriorityRequest(urls, priority);
            }
        } else {
        // 4.获取分片信息
            // 正则匹配 hd-00001.ts
        }


    }

    @Override
    public Site getSite() {
        if (site == null) {
            site = Site.me()
                    .setDomain("geekbang")
                    .addStartUrls("https://time.geekbang.org/serv/v1/column/articles")
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
                    .isDownloadMedia(true)
                    .setMediaDirectory("C:\\QQDownload\\time\\")
                    .setSleepTime(1000);
        }
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new GeekbangTimeProcessor())
                .pipeline(new Log4JPipeline())
                .scheduler(new PriorityScheduler())
                .run();
    }
}
