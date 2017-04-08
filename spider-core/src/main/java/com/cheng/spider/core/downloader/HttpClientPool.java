package com.cheng.spider.core.downloader;

import com.cheng.spider.core.Site;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.*;

import java.util.Map;

/**
 * Desc:
 * Author: 光灿
 * Date: 2017/4/3
 */
public class HttpClientPool {

    public static final HttpClientPool INSTANCE = new HttpClientPool(5);

    private int poolSize;

    public static HttpClientPool getInstance() {
        return INSTANCE;
    }

    private HttpClientPool(int poolSize) {
        this.poolSize = poolSize;
    }

    public HttpClient getClient(Site site) {
        return generateClient(site);
    }

    private HttpClient generateClient(Site site) {
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.USER_AGENT, site.getUserAgent());
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 1000);
        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);

        HttpProtocolParamBean paramBean = new HttpProtocolParamBean(params);
        paramBean.setVersion(HttpVersion.HTTP_1_1);
        paramBean.setContentCharset("UTF-8");
        paramBean.setUseExpectContinue(false);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(schemeRegistry);
        connectionManager.setMaxTotal(poolSize);
        connectionManager.setDefaultMaxPerRoute(100);
        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager, params);
        generateCookie(httpClient, site);
        httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
        return httpClient;
    }

    private void generateCookie(DefaultHttpClient httpClient, Site site) {
        CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setDomain(site.getDomain());
            cookieStore.addCookie(cookie);
        }
        httpClient.setCookieStore(cookieStore);
    }


}
