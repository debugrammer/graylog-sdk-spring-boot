package com.joonsang.graylog.sdk.spring.samples.service;

import com.joonsang.graylog.GraylogQuery;
import com.joonsang.graylog.sdk.spring.samples.domain.GraylogMessage;
import com.joonsang.graylog.sdk.spring.starter.GraylogSearch;
import com.joonsang.graylog.sdk.spring.starter.constant.*;
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
        String from,
        String to,
        GraylogQuery query,
        int pageSize,
        int pageNo
    ) throws IOException {

        Timerange timerange = Timerange.builder().type(TimeRangeType.absolute).from(from).to(to).build();
        SortConfig sort = SortConfig.builder().field("timestamp").order(SortConfigOrder.DESC).build();

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

    public Terms getTerms(String from, String to, GraylogQuery query) throws IOException {
        Timerange timerange = Timerange.builder().type(TimeRangeType.absolute).from(from).to(to).build();

        List<Series> seriesList = List.of(
            Series.builder().type(SeriesType.count).build(),
            Series.builder().type(SeriesType.avg).field("process_time").build()
        );

        List<SearchTypePivot> rowGroups = List.of(
            SearchTypePivot.builder().type(SearchTypePivotType.values).field("client_id").limit(10).build(),
            SearchTypePivot.builder().type(SearchTypePivotType.values).field("client_name").limit(10).build()
        );

        List<SearchTypePivot> columnGroups = List.of(
            SearchTypePivot.builder().type(SearchTypePivotType.values).field("grant_type").limit(5).build()
        );

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

    public Histogram getHistogram(String from, String to, GraylogQuery query) throws IOException {
        Timerange timerange = Timerange.builder().type(TimeRangeType.absolute).from(from).to(to).build();

        Interval interval = Interval.builder()
            .type(IntervalType.timeunit)
            .timeunit(IntervalTimeunit.get(IntervalTimeunit.Unit.minutes, 1))
            .build();

        List<Series> seriesList = List.of(
            Series.builder().type(SeriesType.count).build(),
            Series.builder().type(SeriesType.avg).field("process_time").build()
        );

        List<SearchTypePivot> columnGroups = List.of();

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
