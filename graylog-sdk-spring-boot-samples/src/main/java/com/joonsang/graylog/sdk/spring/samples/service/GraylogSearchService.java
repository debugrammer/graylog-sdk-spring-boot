package com.joonsang.graylog.sdk.spring.samples.service;

import com.joonsang.graylog.GraylogQuery;
import com.joonsang.graylog.sdk.spring.samples.domain.GraylogMessage;
import com.joonsang.graylog.sdk.spring.starter.GraylogSearch;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GraylogSearchService {

    private final String GRAYLOG_STREAM_ID;

    private final GraylogSearch graylogSearch;

    public GraylogSearchService(
        @Value("${graylog.streamId}") String graylogStreamId,
        GraylogSearch graylogSearch
    ) {

        this.GRAYLOG_STREAM_ID = graylogStreamId;
        this.graylogSearch = graylogSearch;
    }

    public Page<GraylogMessage> getMessages(
        Timerange timerange,
        GraylogQuery query,
        int pageSize,
        int pageNo,
        SortConfig sort
    ) throws IOException {

        @SuppressWarnings("unchecked")
        Page<GraylogMessage> messages = (Page<GraylogMessage>) graylogSearch.getMessages(
            List.of(GRAYLOG_STREAM_ID),
            timerange,
            query.build(),
            pageSize,
            pageNo,
            sort,
            GraylogMessage.class
        );

        return messages;
    }

    public List<Statistics> getStatistics(
        Timerange timerange,
        GraylogQuery query,
        List<Series> seriesList
    ) throws IOException {

        return graylogSearch.getStatistics(
            List.of(GRAYLOG_STREAM_ID),
            timerange,
            query.build(),
            seriesList
        );
    }

    public Terms getTerms(
        Timerange timerange,
        GraylogQuery query,
        List<Series> seriesList,
        List<SearchTypePivot> rowGroups,
        List<SearchTypePivot> columnGroups
    ) throws IOException {

        return graylogSearch.getTerms(
            List.of(GRAYLOG_STREAM_ID),
            timerange,
            query.build(),
            seriesList,
            rowGroups,
            columnGroups,
            List.of()
        );
    }

    public Histogram getHistogram(
        Timerange timerange,
        Interval interval,
        GraylogQuery query,
        List<Series> seriesList,
        List<SearchTypePivot> columnGroups
    ) throws IOException {

        return graylogSearch.getHistogram(
            List.of(GRAYLOG_STREAM_ID),
            timerange,
            interval,
            query.build(),
            seriesList,
            columnGroups
        );
    }
}
