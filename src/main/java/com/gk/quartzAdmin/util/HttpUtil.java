package com.gk.quartzAdmin.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;


/**
 * Created by liuhuan on 17/7/12.
 */
public class HttpUtil {


    /**
     * HttpClient单例持有
     */
    private static class HttpClientHolder {
        private static final CloseableHttpClient INSTANCE = HttpClients.custom()
                .disableAutomaticRetries()
                .setMaxConnTotal(10240)
                .setMaxConnPerRoute(512)
                .setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(30 * 1000).setSocketTimeout(30 * 1000).build())
                .setUserAgent("")
                .build();
    }

    /**
     * @return HttpClient单例
     */
    public static CloseableHttpClient getHttpClient() {
        return HttpClientHolder.INSTANCE;
    }


    /**
     * 安静的关闭，即使抛出异常
     *
     * @param response 响应
     */
    public static void closeQuietly(CloseableHttpResponse response) {
        if (response == null) {
            return;
        }
        try {
            response.close();
        } catch (IOException ignored) {
        }
    }

}
