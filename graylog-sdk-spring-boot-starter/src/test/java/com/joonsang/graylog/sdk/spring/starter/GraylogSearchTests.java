package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogSdkAutoConfiguration;
import com.joonsang.graylog.sdk.spring.starter.constant.SortConfigOrder;
import com.joonsang.graylog.sdk.spring.starter.constant.TimeRangeType;
import com.joonsang.graylog.sdk.spring.starter.domain.Page;
import com.joonsang.graylog.sdk.spring.starter.domain.SortConfig;
import com.joonsang.graylog.sdk.spring.starter.domain.Timerange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest(
    classes = {
        GraylogSdkAutoConfiguration.class
    },
    properties = {
        "spring.main.banner-mode=off"
    }
)
public class GraylogSearchTests {

    @Value("${graylog.streamId}")
    String GRAYLOG_STREAM_ID;

    @Autowired
    GraylogSearch graylogSearch;

    @Test
    void pagedMessages() throws IOException {
        Timerange timerange = Timerange.builder().type(TimeRangeType.relative).range(300).build();
        SortConfig sort = SortConfig.builder().field("timestamp").order(SortConfigOrder.DESC).build();

        @SuppressWarnings("unchecked")
        Page<TestMessage> pagedMessages = (Page<TestMessage>) graylogSearch.getMessages(
            List.of(GRAYLOG_STREAM_ID),
            timerange,
            "message:API_REQUEST_FINISHED",
            10,
            1,
            sort,
            TestMessage.class
        );

        System.out.println(pagedMessages.toString());
    }

    @Test
    void sample() throws IOException {
        graylogSearch.sample("message:API_REQUEST_FINISHED", List.of(GRAYLOG_STREAM_ID));
    }
}
