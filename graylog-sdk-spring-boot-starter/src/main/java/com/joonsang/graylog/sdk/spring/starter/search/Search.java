package com.joonsang.graylog.sdk.spring.starter.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.joonsang.graylog.sdk.spring.starter.GraylogRequest;
import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogApiProperties;
import com.joonsang.graylog.sdk.spring.starter.constant.SearchTypeType;
import com.joonsang.graylog.sdk.spring.starter.constant.SeriesType;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
     * @param timerange Graylog time range object
     * @param searchQuery Graylog search query
     * @param limit maximum number of messages to return
     * @param offset offset
     * @param sort Graylog sort config object
     * @param streamIds Graylog stream ID list
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

        SearchType searchType = SearchType.builder()
            .name("messages")
            .limit(limit)
            .offset(offset)
            .sort(List.of(sort))
            .type(SearchTypeType.messages)
            .build();

        Query query = Query.builder()
            .filter(convertToFilter(streamIds))
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
     * Statistics.
     * @param timerange Graylog time range object
     * @param searchQuery Graylog search query
     * @param seriesList Gralog series object list
     * @param streamIds Graylog stream ID list
     * @return Statistics from Graylog
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public List<Statistics> getStatistics(
        Timerange timerange,
        String searchQuery,
        List<Series> seriesList,
        List<String> streamIds
    ) throws IOException {

        SearchType searchType = SearchType.builder()
            .name("chart")
            .series(seriesList)
            .rollup(true)
            .rowGroups(List.of())
            .columnGroups(List.of())
            .sort(List.of())
            .type(SearchTypeType.pivot)
            .build();

        Query query = Query.builder()
            .filter(convertToFilter(streamIds))
            .query(SearchQuery.builder().queryString(searchQuery).build())
            .timerange(timerange)
            .searchType(searchType)
            .build();

        String body = syncSearch(SearchSpec.builder().query(query).build());

        String searchQueryPath = "$.results." + query.getId() + ".query.search_types[0].series";
        String searchResultPath = "$.results." + query.getId() + ".search_types." + searchType.getId() + ".rows[0].values";

        @SuppressWarnings("unchecked")
        List<Map<String, ?>> requestSeries = JsonPath.parse(body).read(searchQueryPath, List.class);

        @SuppressWarnings("unchecked")
        List<Map<String, ?>> values = JsonPath.parse(body).read(searchResultPath, List.class);

        Map<String, Map<String, String>> keyMap = requestSeries.stream()
            .collect(
                Collectors.toMap(
                    map -> (String) map.get("id"),
                    map -> Map.of(
                        "field", (String) map.get("field"),
                        "type", (String) map.get("type"),
                        "percentile", String.valueOf((map.get("percentile")))
                    )
                )
            );

        Map<String, Statistics> statisticsFieldMap = new HashMap<>();

        for (Map<String, ?> valueMap : values) {
            @SuppressWarnings("unchecked")
            List<String> keys = (List<String>) valueMap.get("key");

            String id = keys.get(0);
            String field = keyMap.get(id).get("field");
            String type = keyMap.get(id).get("type");
            String percentile = keyMap.get(id).get("percentile");

            if (!statisticsFieldMap.containsKey(field)) {
                statisticsFieldMap.put(field, new Statistics());
                statisticsFieldMap.get(field).setField(field);
            }

            if (type.equals(SeriesType.avg.toString())) {
                statisticsFieldMap.get(field).setAverage((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.card.toString())) {
                statisticsFieldMap.get(field).setCardinality((Integer) valueMap.get("value"));
            }

            if (type.equals(SeriesType.count.toString())) {
                statisticsFieldMap.get(field).setCount((Integer) valueMap.get("value"));
            }

            if (type.equals(SeriesType.max.toString())) {
                statisticsFieldMap.get(field).setMax((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.min.toString())) {
                statisticsFieldMap.get(field).setMin((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.stddev.toString())) {
                statisticsFieldMap.get(field).setStdDeviation((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.sum.toString())) {
                statisticsFieldMap.get(field).setSum((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.sumofsquares.toString())) {
                statisticsFieldMap.get(field).setSum((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.variance.toString())) {
                statisticsFieldMap.get(field).setSum((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.percentile.toString())) {
                if (statisticsFieldMap.get(field).getPercentiles() == null) {
                    statisticsFieldMap.get(field).setPercentiles(new ArrayList<>());
                }

                if (statisticsFieldMap.get(field).getPercentileRanks() == null) {
                    statisticsFieldMap.get(field).setPercentileRanks(new ArrayList<>());
                }

                statisticsFieldMap.get(field).getPercentiles().add((Double) valueMap.get("value"));
                statisticsFieldMap.get(field).getPercentileRanks().add(percentile);
            }
        }

        return new ArrayList<>(statisticsFieldMap.values());
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

    /**
     * Convert stream ID list to Graylog filter object.
     * @param streamIds Graylog stream ID list
     * @return Graylog filter object
     * @since 2.0.0
     */
    private Filter convertToFilter(List<String> streamIds) {
        List<SearchFilter> filters = streamIds.stream()
            .map(streamId -> SearchFilter.builder().id(streamId).build())
            .collect(Collectors.toList());

        return Filter.builder().filters(filters).build();
    }
}
