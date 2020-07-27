package com.joonsang.graylog.sdk.spring.starter.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.joonsang.graylog.sdk.spring.starter.GraylogRequest;
import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogApiProperties;
import com.joonsang.graylog.sdk.spring.starter.constant.SearchTypePivotType;
import com.joonsang.graylog.sdk.spring.starter.constant.SearchTypeType;
import com.joonsang.graylog.sdk.spring.starter.constant.SeriesType;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
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

        return convertToStats(requestSeries, values);
    }

    /**
     * Terms.
     * @param timerange Graylog time range object
     * @param searchQuery Graylog search query
     * @param seriesList Gralog series object list
     * @param rowGroups Graylog search type pivot object list
     * @param sorts Graylog sort config object list
     * @param streamIds Graylog stream ID list
     * @return Terms from Graylog
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public Terms getTerms(
        Timerange timerange,
        String searchQuery,
        List<Series> seriesList,
        List<SearchTypePivot> rowGroups,
        List<SortConfig> sorts,
        List<String> streamIds
    ) throws IOException {

        SearchType searchType = SearchType.builder()
            .name("chart")
            .series(seriesList)
            .rollup(true)
            .rowGroups(rowGroups)
            .columnGroups(List.of())
            .sort(sorts)
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
        String searchResultPath = "$.results." + query.getId() + ".search_types." + searchType.getId() + ".rows";

        @SuppressWarnings("unchecked")
        List<Map<String, ?>> requestSeries = JsonPath.parse(body).read(searchQueryPath, List.class);

        @SuppressWarnings("unchecked")
        List<Map<String, ?>> results = JsonPath.parse(body).read(searchResultPath, List.class);

        List<Terms.TermsData> termsDataList = new ArrayList<>();

        for (Map<String, ?> result : results) {
            if (!result.get("source").equals("leaf")) {
                continue;
            }

            @SuppressWarnings("unchecked")
            List<String> labels = (List<String>) result.get("key");

            @SuppressWarnings("unchecked")
            List<Map<String, ?>> values = (List<Map<String, ?>>) result.get("values");

            List<Statistics> statsList = convertToStats(requestSeries, values);

            termsDataList.add(Terms.TermsData.builder().labels(labels).statisticsList(statsList).build());
        }

        return Terms.builder().terms(termsDataList).build();
    }

    /**
     * Histogram.
     * @param timerange Graylog time range object
     * @param interval Graylog interval object
     * @param searchQuery Graylog search query
     * @param seriesList Gralog series object list
     * @param columnGroups Graylog search type pivot object list
     * @param streamIds Graylog stream ID list
     * @return Histogram from Graylog
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public void getHistogram(
        Timerange timerange,
        Interval interval,
        String searchQuery,
        List<Series> seriesList,
        List<SearchTypePivot> columnGroups,
        List<String> streamIds
    ) throws IOException {

        SearchType searchType = SearchType.builder()
            .name("chart")
            .series(seriesList)
            .rollup(true)
            .rowGroups(
                List.of(
                    SearchTypePivot.builder()
                        .type(SearchTypePivotType.time)
                        .field("timestamp")
                        .interval(interval)
                        .build()
                )
            )
            .columnGroups(columnGroups)
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

        System.out.println(body);
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
     * Convert Graylog response to Statistics object.
     * @param requestSeries series requested to Graylog
     * @param values values from Graylog
     * @return Statistics object
     * @since 2.0.0
     */
    private List<Statistics> convertToStats(List<Map<String, ?>> requestSeries, List<Map<String, ?>> values) {
        Map<String, Map<String, String>> keyMap = requestSeries.stream()
            .collect(
                Collectors.toMap(
                    series -> (String) series.get("id"),
                    series -> Map.of(
                        "field", Objects.requireNonNullElse((String) series.get("field"), StringUtils.EMPTY),
                        "type", (String) series.get("type"),
                        "percentile", String.valueOf((series.get("percentile")))
                    )
                )
            );

        Map<String, Statistics> statsFieldMap = new HashMap<>();

        for (Map<String, ?> valueMap : values) {
            @SuppressWarnings("unchecked")
            List<String> keys = (List<String>) valueMap.get("key");

            String id = keys.get(0);
            String field = keyMap.get(id).get("field");
            String type = keyMap.get(id).get("type");
            String percentile = keyMap.get(id).get("percentile");

            if (!statsFieldMap.containsKey(field)) {
                statsFieldMap.put(field, new Statistics());
                statsFieldMap.get(field).setField(field);
            }

            if (type.equals(SeriesType.avg.toString())) {
                statsFieldMap.get(field).setAverage((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.card.toString())) {
                statsFieldMap.get(field).setCardinality((Integer) valueMap.get("value"));
            }

            if (type.equals(SeriesType.count.toString())) {
                statsFieldMap.get(field).setCount((Integer) valueMap.get("value"));
            }

            if (type.equals(SeriesType.max.toString())) {
                statsFieldMap.get(field).setMax((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.min.toString())) {
                statsFieldMap.get(field).setMin((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.stddev.toString())) {
                statsFieldMap.get(field).setStdDeviation((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.sum.toString())) {
                statsFieldMap.get(field).setSum((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.sumofsquares.toString())) {
                statsFieldMap.get(field).setSum((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.variance.toString())) {
                statsFieldMap.get(field).setSum((Double) valueMap.get("value"));
            }

            if (type.equals(SeriesType.percentile.toString())) {
                if (statsFieldMap.get(field).getPercentiles() == null) {
                    statsFieldMap.get(field).setPercentiles(new ArrayList<>());
                }

                if (statsFieldMap.get(field).getPercentileRanks() == null) {
                    statsFieldMap.get(field).setPercentileRanks(new ArrayList<>());
                }

                statsFieldMap.get(field).getPercentiles().add((Double) valueMap.get("value"));
                statsFieldMap.get(field).getPercentileRanks().add(percentile);
            }
        }

        return new ArrayList<>(statsFieldMap.values());
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
