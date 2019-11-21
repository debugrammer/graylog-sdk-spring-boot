package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogSdkAutoConfiguration;
import com.joonsang.graylog.sdk.spring.starter.constant.TimeUnit;
import com.joonsang.graylog.sdk.spring.starter.domain.FieldHistogram;
import com.joonsang.graylog.sdk.spring.starter.domain.Histogram;
import com.joonsang.graylog.sdk.spring.starter.domain.Statistics;
import com.joonsang.graylog.sdk.spring.starter.domain.Terms;
import org.apache.commons.lang3.StringUtils;
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
	void statistics() throws IOException {
		LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		Statistics statistics = graylogSearch.getStatistics(
			GRAYLOG_STREAM_ID,
			"process_time",
			from,
			to,
			"message:API_REQUEST_FINISHED"
		);

		assertThat(statistics).isNotNull();
	}

	@Test
	void histogram() throws IOException {
		LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		Histogram histogram = graylogSearch.getHistogram(
			GRAYLOG_STREAM_ID,
			TimeUnit.HOUR,
			from,
			to,
			"message:API_REQUEST_FINISHED"
		);

		assertThat(histogram).isNotNull();
	}

	@Test
	void fieldHistogram() throws IOException {
		LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		FieldHistogram fieldHistogram = graylogSearch.getFieldHistogram(
			GRAYLOG_STREAM_ID,
			"process_time",
			TimeUnit.HOUR,
			from,
			to,
			"message:API_REQUEST_FINISHED"
		);

		assertThat(fieldHistogram).isNotNull();
	}

	@Test
	void terms() throws IOException {
		LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		Terms terms = graylogSearch.getTerms(
			GRAYLOG_STREAM_ID,
			"process_time",
			StringUtils.EMPTY,
			5,
			from,
			to,
			false,
			false,
			"message:API_REQUEST_FINISHED"
		);

		assertThat(terms).isNotNull();
	}
}
