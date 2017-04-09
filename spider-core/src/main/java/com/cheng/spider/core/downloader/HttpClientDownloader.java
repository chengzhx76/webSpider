package com.cheng.spider.core.downloader;

import com.cheng.spider.core.Page;
import com.cheng.spider.core.Request;
import com.cheng.spider.core.Site;
import com.cheng.spider.core.Task;
import com.cheng.spider.core.util.UrlUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/3/26
 */
public class HttpClientDownloader implements Downloader {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Page download(Request request, Task task) {

        Site site = task.getSite();
        HttpClient httpClient = HttpClientPool.getInstance().getClient(site);
        String charset = site.getCharset();
        try {
            HttpGet httpGet = new HttpGet(request.getUrl());
            HttpResponse httpResponse  = null;
            int tried = 0;
            boolean retry;
            do {
                try {
                    httpResponse  = httpClient.execute(httpGet);
                    retry = false;
                } catch (IOException e) {
                    tried++;
                    if (tried > site.getRetryTimes()) {
                        log.warn("download page " + request.getUrl() + " error", e);
                        return null;
                    }
                    log.info("download page " + request.getUrl() + " error, retry the "+tried+" times!");
                    retry = true;
                }
            } while (retry);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (site.getAcceptStatCode().contains(statusCode)) {
                if (charset == null) {
                    String value = httpResponse.getEntity().getContentType().getValue();
                    charset = UrlUtils.getCharset(value);
                }

                String content = IOUtils.toString(httpResponse.getEntity().getContent(), charset);

                Page page = new Page();
                page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(content, request.getUrl())));
                page.setUrl(new PlainText(request.getUrl()));
                page.setRequest(request);
                return page;
            } else {
                log.warn("code error " + statusCode + "\t" + request.getUrl());
            }
        } catch (IOException e) {
            log.warn("download page " + request.getUrl() + " error", e);
        }
        return null;
    }
}
