package com.cheng.spider.core.downloader;

import com.cheng.spider.core.*;
import com.cheng.spider.core.util.Constant;
import com.google.common.base.Strings;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/4/22
 */
public class MediaDownloader implements Downloader {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Page download(Request request, Task task) {
        Site site = task.getSite();
        String directory = site.getDirectory();
        HttpClient httpClient = HttpClientPool.getInstance().getClient(site);
        HttpGet httpGet = new HttpGet(request.getUrl());
        HttpResponse httpResponse;
        try {
            httpResponse  = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
                directory = getAllDirectory(directory, request);
                String mediaName = getMediaName(request);
                InputStream input = null;
                FileOutputStream output = null;
                try {
                    input = entity.getContent();
                    File fileDir = new File(directory);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    output = new FileOutputStream(new File(directory + mediaName));
                    byte[] buf = new byte[1024];
                    int length;
                    while ((length = input.read(buf, 0, buf.length)) != -1) {
                        output.write(buf, 0, length);
                    }

                    Page page = new Page();
                    page.putField(Constant.ID, mediaName);
                    Object title = request.getExtra(Constant.TITLE);
                    if (title != null) {
                        page.putField(Constant.TITLE, title.toString());
                    }
                    page.setRequest(request);
                    return page;

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (input != null) input.close();
                        if (output != null) output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                log.warn("code error " + statusCode + "\t" + request.getUrl());
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取全路径
     * @param request
     * @return
     */
    private String getAllDirectory(String directory, Request request) {
        StringBuilder allDir = new StringBuilder(directory);
        if (!Strings.isNullOrEmpty(request.getSubdires())) {
            allDir.append(File.separator).append(request.getSubdires()).append(File.separator);
        }
        return allDir.toString();
    }
    private String getMediaName(Request request) {
        return request.getUrl().substring(request.getUrl().lastIndexOf("/") + 1, request.getUrl().length());
    }

}
