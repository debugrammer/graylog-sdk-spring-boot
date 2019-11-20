package com.joonsang.graylog.sdk.spring.starter.autoconfigure;

import com.joonsang.graylog.sdk.spring.starter.GraylogSearch;
import com.joonsang.graylog.sdk.spring.starter.GraylogRequest;
import com.joonsang.graylog.sdk.spring.starter.search.SearchAbsolute;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Graylog SDK Auto Configuration
 * @author debugrammer
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({ GraylogSdkProperties.class, GraylogApiProperties.class })
public class GraylogSdkAutoConfiguration {

    private final GraylogSdkProperties graylogSdkProperties;

    private final GraylogApiProperties graylogApiProperties;

    public GraylogSdkAutoConfiguration(
        GraylogSdkProperties graylogSdkProperties,
        GraylogApiProperties graylogApiProperties
    ) {

        this.graylogSdkProperties = graylogSdkProperties;
        this.graylogApiProperties = graylogApiProperties;
    }

    @Bean
    public OkHttpClient graylogOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(10, 10, TimeUnit.SECONDS))
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS);

        builder.networkInterceptors().add(chain -> {
            Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Basic " + graylogApiProperties.getCredentials())
                .addHeader("Accept", "application/json")
                .build();

            return chain.proceed(request);
        });

        return builder.build();
    }

    @Bean
    public GraylogSearch graylogSearch(
        @Qualifier("graylogOkHttpClient") OkHttpClient okHttpClient
    ) {

        GraylogRequest request = new GraylogRequest(okHttpClient, graylogApiProperties);
        SearchAbsolute absolute = new SearchAbsolute(request, graylogSdkProperties);

        return new GraylogSearch(absolute);
    }
}
