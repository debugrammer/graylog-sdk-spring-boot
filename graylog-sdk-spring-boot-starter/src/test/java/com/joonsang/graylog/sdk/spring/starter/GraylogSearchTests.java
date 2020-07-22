package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogSdkAutoConfiguration;
import com.joonsang.graylog.sdk.spring.starter.constant.SearchTypeType;
import com.joonsang.graylog.sdk.spring.starter.constant.SortConfigOrder;
import com.joonsang.graylog.sdk.spring.starter.constant.TimeRangeType;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    void raw() throws IOException {
        List<String> streamIds = List.of(GRAYLOG_STREAM_ID);

        List<SearchFilter> filters = streamIds.stream()
            .map(streamId -> SearchFilter.builder().id(streamId).build())
            .collect(Collectors.toList());

        SearchSpec searchSpec = SearchSpec.builder()
            .query(
                Query.builder()
                    .filter(Filter.builder().filters(filters).build())
                    .query(SearchQuery.builder().queryString("message:API_REQUEST_FINISHED").build())
                    .timerange(Timerange.builder().type(TimeRangeType.relative).range(300).build())
                    .searchType(
                        SearchType.builder()
                            .name("chart")
                            .series(List.of(Series.builder().id("count()").type("count").build()))
                            .rollup(true)
                            .rowGroups(
                                List.of(SearchTypePivot.builder().type("values").field("client_name").limit(15).build())
                            )
                            .columnGroups(List.of())
                            .sort(List.of())
                            .type(SearchTypeType.pivot)
                            .build()
                    )
                    .build()
            )
            .build();

        System.out.println(graylogSearch.raw(searchSpec));
    }
}
