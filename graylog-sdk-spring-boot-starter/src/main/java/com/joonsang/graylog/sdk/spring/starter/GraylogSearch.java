package com.joonsang.graylog.sdk.spring.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import com.joonsang.graylog.sdk.spring.starter.search.Search;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Graylog Search
 * @author debugrammer
 * @since 2.0.0
 */
public class GraylogSearch {

    private final ObjectMapper objectMapper;

    private final Search search;

    public GraylogSearch(ObjectMapper objectMapper, Search search) {
        this.objectMapper = objectMapper;
        this.search = search;
    }

    /**
     * Message list with paging.
     * @param streamIds Graylog stream ID list
     * @param timerange Graylog time range object
     * @param searchQuery Graylog search query
     * @param pageSize size of each page
     * @param pageNo page number
     * @param sort Graylog sort config object
     * @param messageObject message object
     * @return List of message with paging
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public Page<?> getMessages(
        List<String> streamIds,
        Timerange timerange,
        String searchQuery,
        int pageSize,
        int pageNo,
        SortConfig sort,
        Class<?> messageObject
    ) throws IOException {

        int offset = (pageSize * pageNo) - pageSize;

        MessageList messageList = search.getMessages(timerange, searchQuery, pageSize, offset, sort, streamIds);

        List<Map<String, Map<String, ?>>> messageMapList = messageList.getMessages();

        return Page.builder()
            .pageNo(pageNo)
            .pageSize(pageSize)
            .list(
                messageMapList.stream()
                    .map(e -> objectMapper.convertValue(e.get("message"), messageObject))
                    .collect(Collectors.toList())
            )
            .totalCount(messageList.getTotalCount())
            .build();
    }

    /**
     * Statistics.
     * @param streamIds Graylog stream ID list
     * @param timerange Graylog time range object
     * @param searchQuery Graylog search query
     * @param seriesList Gralog series object list
     * @return Statistics from Graylog
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public List<Statistics> getStatistics(
        List<String> streamIds,
        Timerange timerange,
        String searchQuery,
        List<Series> seriesList
    ) throws IOException {

        return search.getStatistics(timerange, searchQuery, seriesList, streamIds);
    }

    /**
     * Terms.
     * @param streamIds Graylog stream ID list
     * @param timerange Graylog time range object
     * @param searchQuery Graylog search query
     * @param seriesList Gralog series object list
     * @param rowGroups Graylog search type pivot object list
     * @param columnGroups Graylog search type pivot object list
     * @param sorts Graylog sort config object list
     * @return Terms from Graylog
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public Terms getTerms(
        List<String> streamIds,
        Timerange timerange,
        String searchQuery,
        List<Series> seriesList,
        List<SearchTypePivot> rowGroups,
        List<SearchTypePivot> columnGroups,
        List<SortConfig> sorts
    ) throws IOException {

        return search.getTerms(timerange, searchQuery, seriesList, rowGroups, columnGroups, sorts, streamIds);
    }

    /**
     * Histogram.
     * @param streamIds Graylog stream ID list
     * @param timerange Graylog time range object
     * @param interval Graylog interval object
     * @param searchQuery Graylog search query
     * @param seriesList Gralog series object list
     * @param columnGroups Graylog search type pivot object list
     * @return Histogram from Graylog
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public Histogram getHistogram(
        List<String> streamIds,
        Timerange timerange,
        Interval interval,
        String searchQuery,
        List<Series> seriesList,
        List<SearchTypePivot> columnGroups
    ) throws IOException {

        return search.getHistogram(timerange, interval, searchQuery, seriesList, columnGroups, streamIds);
    }

    /**
     * Raw search.
     * @param searchSpec Graylog search spec object
     * @return Response body from Graylog
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public String raw(SearchSpec searchSpec) throws IOException {
        return search.syncSearch(searchSpec);
    }
}
