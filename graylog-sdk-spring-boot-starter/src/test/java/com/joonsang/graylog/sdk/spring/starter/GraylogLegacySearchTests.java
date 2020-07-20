package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogSdkAutoConfiguration;
import com.joonsang.graylog.sdk.spring.starter.constant.TimeUnit;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
	classes = {
		GraylogSdkAutoConfiguration.class
	},
	properties = {
		"spring.main.banner-mode=off"
	}
)
class GraylogLegacySearchTests {

	@Value("${graylog.streamId}")
	String GRAYLOG_STREAM_ID;

	@Autowired
	GraylogLegacySearch graylogLegacySearch;

	@Test
	void messages() throws IOException, ReflectiveOperationException {
		LocalDateTime from = LocalDateTime.now().minusDays(1L);
		LocalDateTime to = LocalDateTime.now();

		@SuppressWarnings("unchecked")
		List<TestMessage> messages = (List<TestMessage>) graylogLegacySearch.getMessages(
			GRAYLOG_STREAM_ID,
			from,
			to,
			"message:API_REQUEST_FINISHED",
			TestMessage.class
		);

		assertThat(messages).isNotEmpty();
	}

	@Test
	void pagedMessages() throws IOException, ReflectiveOperationException {
		LocalDateTime from = LocalDateTime.now().minusDays(2L);
		LocalDateTime to = LocalDateTime.now().minusDays(1L);

		@SuppressWarnings("unchecked")
		Page<TestMessage> pagedMessages = (Page<TestMessage>) graylogLegacySearch.getMessages(
			GRAYLOG_STREAM_ID,
			from,
			to,
			"message:API_REQUEST_FINISHED",
			5,
			1,
			TestMessage.class
		);

		assertThat(pagedMessages.getList()).isNotEmpty();
		assertThat(pagedMessages.getTotalCount()).isNotZero();
	}

	@Test
	void statistics() throws IOException {
		LocalDateTime from = LocalDateTime.now().minusDays(1L);
		LocalDateTime to = LocalDateTime.now();

		Statistics statistics = graylogLegacySearch.getStatistics(
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
		LocalDateTime from = LocalDateTime.now().minusDays(1L);
		LocalDateTime to = LocalDateTime.now();

		Histogram histogram = graylogLegacySearch.getHistogram(
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
		LocalDateTime from = LocalDateTime.now().minusDays(1L);
		LocalDateTime to = LocalDateTime.now();

		FieldHistogram fieldHistogram = graylogLegacySearch.getFieldHistogram(
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
		LocalDateTime from = LocalDateTime.now().minusDays(1L);
		LocalDateTime to = LocalDateTime.now();

		Terms terms = graylogLegacySearch.getTerms(
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
