package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogSdkAutoConfiguration;
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
    void sample() throws IOException {
        graylogSearch.sample(List.of(GRAYLOG_STREAM_ID));
    }
}
