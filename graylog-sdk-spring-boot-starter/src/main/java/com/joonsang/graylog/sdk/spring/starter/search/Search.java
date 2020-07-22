package com.joonsang.graylog.sdk.spring.starter.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.joonsang.graylog.sdk.spring.starter.GraylogRequest;
import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogApiProperties;
import com.joonsang.graylog.sdk.spring.starter.constant.SearchTypeType;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Search
 * @author debugrammer
 * @since 2.0.0
 */
public class Search {

    private static final MediaType CONTENT_TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    private final GraylogRequest graylogRequest;

    private final GraylogApiProperties graylogApiProperties;

    private final ObjectMapper objectMapper;

    public Search(
        GraylogRequest graylogRequest,
        GraylogApiProperties graylogApiProperties,
        ObjectMapper objectMapper
    ) {

        this.graylogRequest = graylogRequest;
        this.graylogApiProperties = graylogApiProperties;
        this.objectMapper = objectMapper;
    }

    /**
     * Message list search.
     * @param timerange time range object
     * @param searchQuery Graylog search query
     * @param limit maximum number of messages to return
     * @param offset offset
     * @param sort sort config object
     * @param streamIds Graylog Stream ID list
     * @return Message list from Graylog
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public MessageList getMessages(
        Timerange timerange,
        String searchQuery,
        int limit,
        int offset,
        SortConfig sort,
        List<String> streamIds
    ) throws IOException {

        List<SearchFilter> filters = streamIds.stream()
            .map(streamId -> SearchFilter.builder().id(streamId).build())
            .collect(Collectors.toList());

        SearchType searchType = SearchType.builder()
            .name("messages")
            .limit(limit)
            .offset(offset)
            .sort(List.of(sort))
            .type(SearchTypeType.messages)
            .build();

        Query query = Query.builder()
            .filter(Filter.builder().filters(filters).build())
            .query(SearchQuery.builder().queryString(searchQuery).build())
            .timerange(timerange)
            .searchType(searchType)
            .build();

        String body = syncSearch(SearchSpec.builder().query(query).build());

        String searchResultPath = "$.results." + query.getId() + ".search_types." + searchType.getId();

        @SuppressWarnings("unchecked")
        List<Map<String, Map<String, ?>>> messages = JsonPath.parse(body).read(searchResultPath + ".messages", List.class);
        Integer totalCount = JsonPath.parse(body).read(searchResultPath + ".total_results", Integer.class);

        return MessageList.builder()
            .messages(messages)
            .totalCount(totalCount)
            .build();
    }

    /**
     * Perform synchronous search.
     * @param searchSpec Graylog search spec object
     * @return Response body from Graylog
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public String syncSearch(SearchSpec searchSpec) throws IOException {
        String requestJson = objectMapper.writeValueAsString(searchSpec);
        RequestBody jsonBody = RequestBody.create(requestJson, CONTENT_TYPE_JSON);

        HttpUrl httpUrl = graylogRequest.getHttpUrlBuilder()
            .addPathSegments("api/views/search/sync")
            .addQueryParameter("timeout", String.valueOf(graylogApiProperties.getTimeout()))
            .build();

        return graylogRequest.httpPostRequest(httpUrl, jsonBody);
    }
}
