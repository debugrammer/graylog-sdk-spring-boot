package com.joonsang.graylog.sdk.spring.samples;

import com.joonsang.graylog.GraylogQuery;
import com.joonsang.graylog.sdk.spring.starter.GraylogSearch;
import com.joonsang.graylog.sdk.spring.starter.domain.Statistics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class GraylogSearchSample {

    private final String GRAYLOG_STREAM_ID;

    private final GraylogSearch graylogSearch;

    public GraylogSearchSample(
        @Value("${graylog.streamId}") String graylogStreamId,
        GraylogSearch graylogSearch
    ) {

        this.GRAYLOG_STREAM_ID = graylogStreamId;
        this.graylogSearch = graylogSearch;
    }

    public Statistics statistics() throws IOException {
        LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        GraylogQuery query = GraylogQuery.builder()
            .field("message", "API_REQUEST_FINISHED");

        return graylogSearch.getStatistics(
            GRAYLOG_STREAM_ID,
            "process_time",
            from,
            to,
            query.build()
        );
    }
}
