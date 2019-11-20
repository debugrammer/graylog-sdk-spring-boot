package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogProperties;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

/**
 * Graylog REST API Request
 * @author debugrammer
 * @version 1.0
 * @since 2019-11-19
 */
public class GraylogRequest {

    private final OkHttpClient okHttpClient;

    private final GraylogProperties graylogProperties;

    public GraylogRequest(
        OkHttpClient okHttpClient,
        GraylogProperties graylogProperties
    ) {

        this.okHttpClient = okHttpClient;
        this.graylogProperties = graylogProperties;
    }

    public String httpGetRequest(HttpUrl httpUrl) throws IOException {
        Request request = new Request.Builder()
            .url(httpUrl)
            .get()
            .build();

        Response response = okHttpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Graylog server communication error.");
        }

        if (response.body() == null) {
            throw new IOException("Graylog server responded empty HTTP response body.");
        }

        return Objects.requireNonNull(response.body()).string();
    }

    public HttpUrl.Builder getHttpUrlBuilder() {
        return new HttpUrl.Builder()
            .scheme(graylogProperties.getScheme())
            .host(graylogProperties.getHost())
            .port(graylogProperties.getPort());
    }
}
