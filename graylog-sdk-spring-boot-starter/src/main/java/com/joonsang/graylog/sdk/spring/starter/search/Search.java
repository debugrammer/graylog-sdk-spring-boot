package com.joonsang.graylog.sdk.spring.starter.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import com.joonsang.graylog.sdk.spring.starter.GraylogRequest;
import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogSdkProperties;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Search
 * @author debugrammer
 * @since 2.0.0
 */
public class Search {

    private static final MediaType CONTENT_TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    private final GraylogRequest graylogRequest;

    private final GraylogSdkProperties graylogSdkProperties;

    private final ObjectMapper objectMapper;

    public Search(
        GraylogRequest graylogRequest,
        GraylogSdkProperties graylogSdkProperties,
        ObjectMapper objectMapper
    ) {

        this.graylogRequest = graylogRequest;
        this.graylogSdkProperties = graylogSdkProperties;
        this.objectMapper = objectMapper;
    }

    public void sample(List<String> streamIds) throws IOException {
        List<SearchFilter> filters = new ArrayList<>();

        for (String streamId : streamIds) {
            filters.add(SearchFilter.builder().id(streamId).build());
        }

        com.joonsang.graylog.sdk.spring.starter.domain.Search search =
            com.joonsang.graylog.sdk.spring.starter.domain.Search.builder()
                .query(
                    Query.builder()
                        .filter(Filter.builder().filters(filters).build())
                        .query(SearchQuery.builder().build())
                        .timerange(Timerange.builder().type("relative").range(300).build())
                        .searchType(
                            SearchType.builder()
                                .name("chart")
                                .series(List.of(Series.builder().id("count()").type("count").build()))
                                .rollup(true)
                                .rowGroup(
                                    SearchTypePivot.builder().type("values").field("client_name").limit(15).build()
                                )
                                .sort(List.of())
                                .type("pivot")
                                .build()
                        )
                        .build()
                )
                .build();

        String requestJson = objectMapper.writeValueAsString(search);
        RequestBody jsonBody = RequestBody.create(requestJson, CONTENT_TYPE_JSON);

        HttpUrl httpUrl = graylogRequest.getHttpUrlBuilder()
            .addPathSegments("api/views/search/sync")
            .addQueryParameter("timeout", "60000")
            .build();

        String body = graylogRequest.httpPostRequest(httpUrl, jsonBody);

        System.out.println(body);
    }
}
