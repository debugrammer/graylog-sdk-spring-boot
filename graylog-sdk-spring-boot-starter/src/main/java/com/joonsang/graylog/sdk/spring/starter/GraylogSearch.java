package com.joonsang.graylog.sdk.spring.starter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joonsang.graylog.sdk.spring.starter.domain.FieldHistogram;
import com.joonsang.graylog.sdk.spring.starter.domain.Histogram;
import com.joonsang.graylog.sdk.spring.starter.domain.Terms;
import com.joonsang.graylog.sdk.spring.starter.search.SearchAbsolute;
import com.joonsang.graylog.sdk.spring.starter.domain.Statistics;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Graylog Search
 * @author debugrammer
 * @since 1.0.0
 */
public class GraylogSearch {

    private final ObjectMapper objectMapper;

    private final SearchAbsolute searchAbsolute;

    public GraylogSearch(
        ObjectMapper objectMapper,
        SearchAbsolute searchAbsolute
    ) {

        this.objectMapper = objectMapper;
        this.searchAbsolute = searchAbsolute;
    }

    /**
     * Message list.
     * @param streamId Graylog Stream ID
     * @param fromDateTime time range start
     * @param toDateTime time range end
     * @param query Graylog search query
     * @param messageObject message object
     * @return List of message
     * @throws IOException Graylog server failure
     * @throws NoSuchMethodException if given message object does not have constructor
     * @throws IllegalAccessException if given message object fails initialization
     * @throws InvocationTargetException if given message object fails initialization
     * @throws InstantiationException if given message object fails initialization
     * @since 1.0.0
     */
    public List<?> getMessages(
        String streamId,
        LocalDateTime fromDateTime,
        LocalDateTime toDateTime,
        String query,
        Class<?> messageObject
    ) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String filter = "streams:" + streamId;
        String from = fromDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String to = toDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, Object> objectMap = objectMapper.convertValue(
            messageObject.getDeclaredConstructor().newInstance(),
            new TypeReference<>() {}
        );

        List<String> fieldList = new ArrayList<>(objectMap.keySet());

        List<Map<String, Map<String, ?>>> messageMapList = searchAbsolute.getMessages(
            StringUtils.join(fieldList, ","),
            query,
            from,
            to,
            filter
        );

        return messageMapList
            .stream()
            .map(e -> objectMapper.convertValue(e.get("message"), messageObject))
            .collect(Collectors.toList());
    }

    /**
     * Statistics.
     * @param streamId Graylog Stream ID
     * @param field field name
     * @param fromDateTime time range start
     * @param toDateTime time range end
     * @param query Graylog search query
     * @return Statistics
     * @throws IOException Graylog server failure
     * @since 1.0.0
     */
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

    /**
     * Histogram.
     * @param streamId Graylog Stream ID
     * @param interval histogram interval
     * @param fromDateTime time range start
     * @param toDateTime time range end
     * @param query Graylog search query
     * @return Histogram
     * @throws IOException Graylog server failure
     * @since 1.0.0
     */
    public Histogram getHistogram(
        String streamId,
        String interval,
        LocalDateTime fromDateTime,
        LocalDateTime toDateTime,
        String query
    ) throws IOException {

        String filter = "streams:" + streamId;
        String from = fromDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String to = toDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return searchAbsolute.getHistogram(query, interval, from, to, filter);
    }

    /**
     * Field Histogram.
     * @param streamId Graylog Stream ID
     * @param field field name
     * @param interval histogram interval
     * @param fromDateTime time range start
     * @param toDateTime time range end
     * @param query Graylog search query
     * @return Field Histogram
     * @throws IOException Graylog server failure
     * @since 1.0.0
     */
    public FieldHistogram getFieldHistogram(
        String streamId,
        String field,
        String interval,
        LocalDateTime fromDateTime,
        LocalDateTime toDateTime,
        String query
    ) throws IOException {

        String filter = "streams:" + streamId;
        String from = fromDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String to = toDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return searchAbsolute.getFieldHistogram(field, query, interval, from, to, filter);
    }

    /**
     * Terms.
     * @param streamId Graylog Stream ID
     * @param field field name
     * @param stackedFields field names to stack
     * @param size maximum number of terms to return
     * @param fromDateTime time range start
     * @param toDateTime time range end
     * @param reverseOrder true for ascending order
     * @param topValuesOnly remove other data from result
     * @param query Graylog search query
     * @return Terms
     * @throws IOException Graylog server failure
     * @since 1.0.0
     */
    public Terms getTerms(
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
