package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogSdkAutoConfiguration;
import com.joonsang.graylog.sdk.spring.starter.domain.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
	classes = {
		GraylogSdkAutoConfiguration.class
	},
	properties = {
		"spring.main.banner-mode=off"
	}
)
class GraylogSearchTests {

	@Value("${graylog.streamId}")
	String GRAYLOG_STREAM_ID;

	@Autowired
	GraylogSearch graylogSearch;

	@Test
	void TC_001_STATISTICS() throws IOException {
		LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		Statistics statistics = graylogSearch.getStatistics(
			GRAYLOG_STREAM_ID,
			"process_time",
			from,
			to,
			"message:API_REQUEST_FINISHED"
		);

		assertThat(statistics)
			.isNotNull();
	}
}
