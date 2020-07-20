package com.joonsang.graylog.sdk.spring.samples.service;

import com.joonsang.graylog.GraylogQuery;
import com.joonsang.graylog.sdk.spring.samples.domain.FieldHistograms;
import com.joonsang.graylog.sdk.spring.samples.domain.GraylogMessage;
import com.joonsang.graylog.sdk.spring.samples.domain.Histograms;
import com.joonsang.graylog.sdk.spring.samples.domain.TwoStatistics;
import com.joonsang.graylog.sdk.spring.starter.LegacyGraylogSearch;
import com.joonsang.graylog.sdk.spring.starter.domain.FieldHistogram;
import com.joonsang.graylog.sdk.spring.starter.domain.Histogram;
import com.joonsang.graylog.sdk.spring.starter.domain.Terms;
import com.joonsang.graylog.sdk.spring.starter.domain.TermsData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class GraylogSearchService {

    private final String GRAYLOG_STREAM_ID;

    private final LegacyGraylogSearch legacyGraylogSearch;

    public GraylogSearchService(
        @Value("${graylog.streamId}") String graylogStreamId,
        LegacyGraylogSearch legacyGraylogSearch
    ) {

        this.GRAYLOG_STREAM_ID = graylogStreamId;
        this.legacyGraylogSearch = legacyGraylogSearch;
    }

    public GraylogMessage getMessage(
        LocalDateTime fromDateTime,
        LocalDateTime toDateTime,
        GraylogQuery query
    ) throws IOException, ReflectiveOperationException {

        @SuppressWarnings("unchecked")
        List<GraylogMessage> messages = (List<GraylogMessage>) legacyGraylogSearch.getMessages(
            GRAYLOG_STREAM_ID,
            fromDateTime,
            toDateTime,
            query.build(),
            GraylogMessage.class
        );

        if (messages.isEmpty()) {
            return null;
        }

        return messages.get(0);
    }

    public TwoStatistics getTwoStats(
        String field,
        LocalDateTime firstDateTime,
        LocalDateTime secondDateTime,
        GraylogQuery query
    ) throws IOException {

        LocalDateTime toDateTime = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS);

        return TwoStatistics.builder()
            .first(
                legacyGraylogSearch.getStatistics(
                    GRAYLOG_STREAM_ID,
                    field,
                    firstDateTime,
                    toDateTime,
                    query.build()
                )
            )
            .second(
                legacyGraylogSearch.getStatistics(
                    GRAYLOG_STREAM_ID,
                    field,
                    secondDateTime,
                    toDateTime,
                    query.build()
                )
            )
            .build();
    }

    public Histograms getProcessTimeHistograms(
        String interval,
        LocalDateTime fromDateTime,
        LocalDateTime toDateTime,
        GraylogQuery query
    ) throws IOException {

        Histogram all = legacyGraylogSearch.getHistogram(
            GRAYLOG_STREAM_ID,
            interval,
            fromDateTime,
            toDateTime,
            GraylogQuery.builder(query)
                .and().field("process_time", ">=", 0)
                .build()
        );

        Histogram first = legacyGraylogSearch.getHistogram(
            GRAYLOG_STREAM_ID,
            interval,
            fromDateTime,
            toDateTime,
            GraylogQuery.builder(query)
                .and().range("process_time", "[", 0, 500, "]")
                .build()
        );

        Histogram second = legacyGraylogSearch.getHistogram(
            GRAYLOG_STREAM_ID,
            interval,
            fromDateTime,
            toDateTime,
            GraylogQuery.builder(query)
                .and().field("process_time", ">", 500)
                .build()
        );

        return Histograms.builder()
            .labels(List.of("All", "0-500", "500-"))
            .histograms(List.of(all, first, second))
            .build();
    }

    public FieldHistograms getProcessTimeFieldHistogramsByTopSources(
        int size,
        String interval,
        LocalDateTime fromDateTime,
        LocalDateTime toDateTime,
        GraylogQuery query
    ) throws IOException {

        Terms sourceRanking = legacyGraylogSearch.getTerms(
            GRAYLOG_STREAM_ID,
            "source",
            "",
            size,
            fromDateTime,
            toDateTime,
            false,
            true,
            query.build()
        );

        List<String> labels = new ArrayList<>();
        List<FieldHistogram> fieldHistograms = new ArrayList<>();

        for (TermsData termsData : sourceRanking.getTerms()) {
            String source = termsData.getLabels().get(0);

            labels.add(source);
            fieldHistograms.add(
                legacyGraylogSearch.getFieldHistogram(
                    GRAYLOG_STREAM_ID,
                    "process_time",
                    interval,
                    fromDateTime,
                    toDateTime,
                    GraylogQuery.builder(query)
                        .and().field("source", source)
                        .build()
                )
            );
        }

        return FieldHistograms.builder()
            .labels(labels)
            .fieldHistograms(fieldHistograms)
            .build();
    }

    public Terms getUsageRanking(
        String field,
        String stackedFields,
        int size,
        LocalDateTime fromDateTime,
        LocalDateTime toDateTime,
        boolean reverseOrder,
        boolean topValuesOnly,
        GraylogQuery query
    ) throws IOException {

        return legacyGraylogSearch.getTerms(
            GRAYLOG_STREAM_ID,
            field,
            stackedFields,
            size,
            fromDateTime,
            toDateTime,
            reverseOrder,
            topValuesOnly,
            query.build()
        );
    }
}
