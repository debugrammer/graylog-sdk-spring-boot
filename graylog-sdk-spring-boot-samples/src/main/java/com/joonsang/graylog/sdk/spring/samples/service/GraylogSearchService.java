package com.joonsang.graylog.sdk.spring.samples.service;

import com.joonsang.graylog.GraylogQuery;
import com.joonsang.graylog.sdk.spring.samples.domain.GraylogMessage;
import com.joonsang.graylog.sdk.spring.starter.GraylogSearch;
import com.joonsang.graylog.sdk.spring.starter.constant.SortConfigOrder;
import com.joonsang.graylog.sdk.spring.starter.constant.TimeRangeType;
import com.joonsang.graylog.sdk.spring.starter.domain.Page;
import com.joonsang.graylog.sdk.spring.starter.domain.SortConfig;
import com.joonsang.graylog.sdk.spring.starter.domain.Timerange;
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
}
