package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.domain.Terms;
import com.joonsang.graylog.sdk.spring.starter.search.SearchAbsolute;
import com.joonsang.graylog.sdk.spring.starter.domain.Statistics;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Graylog Search
 * @author debugrammer
 * @since 1.0.0
 */
public class GraylogSearch {

    private final SearchAbsolute searchAbsolute;

    public GraylogSearch(SearchAbsolute searchAbsolute) {
        this.searchAbsolute = searchAbsolute;
    }

    public Statistics getStatistics(
        String streamId,
        String field,
        LocalDateTime fromDateTime,
        LocalDateTime toDateTime,
        String query
    ) throws IOException {

        String filter = "streams:" + streamId;
        String from = fromDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String to = toDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return searchAbsolute.getStatistics(field, query, from, to, filter);
    }

    Terms getTerms(
        String streamId,
        String field,
        String stackedFields,
        int size,
        LocalDateTime fromDateTime,
        LocalDateTime toDateTime,
        boolean reverseOrder,
        boolean topValuesOnly,
        String query
    ) throws IOException {

        String filter = "streams:" + streamId;
        String from = fromDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String to = toDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return searchAbsolute.getTerms(
            field,
            stackedFields,
            query,
            from,
            to,
            filter,
            size,
            reverseOrder,
            topValuesOnly
        );
    }
}
