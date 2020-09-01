package com.joonsang.graylog.sdk.spring.samples.controller;

import com.joonsang.graylog.GraylogQuery;
import com.joonsang.graylog.sdk.spring.samples.domain.GraylogMessage;
import com.joonsang.graylog.sdk.spring.samples.service.GraylogSearchService;
import com.joonsang.graylog.sdk.spring.starter.constant.*;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/v2/search"})
public class GraylogSearchController {

    private final GraylogSearchService graylogSearchService;

    public GraylogSearchController(GraylogSearchService graylogSearchService) {
        this.graylogSearchService = graylogSearchService;
    }

    /**
     * Messages.
     * Get successful messages with paging for last 5 minutes
     */
    @GetMapping({"/messages/successes"})
    public ResponseEntity<?> getSuccessfulMessages(
        @RequestParam(value = "page_no", defaultValue = "1") int pageNo,
        @RequestParam(value = "page_size", defaultValue = "10") int pageSize
    ) throws IOException {

        Timerange timerange = Timerange.builder().type(TimeRangeType.relative).range(300).build();
        SortConfig sort = SortConfig.builder().field("timestamp").order(SortConfigOrder.DESC).build();

        Page<GraylogMessage> messages = graylogSearchService.getMessages(
            timerange,
            GraylogQuery.builder()
                .field("message", "API_REQUEST_FINISHED"),
            pageSize,
            pageNo,
            sort
        );

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    /**
     * Statistics.
     * Get statistics of process time with cardinality of sources and request amounts for last 5 minutes
     */
    @GetMapping({"/statistics/process-times"})
    public ResponseEntity<?> getProcessTimeStats() throws IOException {
        Timerange timerange = Timerange.builder().type(TimeRangeType.relative).range(300).build();
        List<Series> seriesList = List.of(
            Series.builder().type(SeriesType.avg).field("process_time").build(),
            Series.builder().type(SeriesType.count).field("process_time").build(),
            Series.builder().type(SeriesType.min).field("process_time").build(),
            Series.builder().type(SeriesType.max).field("process_time").build(),
            Series.builder().type(SeriesType.percentile).percentile(95.0f).field("process_time").build(),
            Series.builder().type(SeriesType.percentile).percentile(99.0f).field("process_time").build(),
            Series.builder().type(SeriesType.count).build(),
            Series.builder().type(SeriesType.card).field("source").build()
        );

        List<Statistics> stats = graylogSearchService.getStatistics(
            timerange,
            GraylogQuery.builder()
                .field("message", "API_REQUEST_FINISHED"),
            seriesList
        );

        return new ResponseEntity<>(Map.of("statistics", stats), HttpStatus.OK);
    }

    /**
     * Terms.
     * Get rankings of client ID/name by usages/average process times with clients' grant types
     */
    @GetMapping({"/terms/clients"})
    public ResponseEntity<?> getClientTerms(
        @RequestParam(value = "from") String from,
        @RequestParam(value = "to") String to
    ) throws IOException {

        Timerange timerange = Timerange.builder().type(TimeRangeType.absolute).from(from).to(to).build();

        List<Series> seriesList = List.of(
            Series.builder().type(SeriesType.count).build(),
            Series.builder().type(SeriesType.avg).field("process_time").build()
        );

        List<SearchTypePivot> rowGroups = List.of(
            SearchTypePivot.builder().type(SearchTypePivotType.values).field("client_id").limit(10).build(),
            SearchTypePivot.builder().type(SearchTypePivotType.values).field("client_name").build()
        );

        List<SearchTypePivot> columnGroups = List.of(
            SearchTypePivot.builder().type(SearchTypePivotType.values).field("grant_type").limit(5).build()
        );

        SortConfig sort = SortConfig.builder()
            .type(SortConfigType.series)
            .field("count()")
            .direction(SortConfigDirection.Descending)
            .build();

        Terms terms = graylogSearchService.getTerms(
            timerange,
            GraylogQuery.builder()
                .field("message", "API_REQUEST_FINISHED"),
            seriesList,
            rowGroups,
            columnGroups,
            sort
        );

        return new ResponseEntity<>(terms, HttpStatus.OK);
    }

    /**
     * Histogram.
     * Get top 5 clients' request amounts with average process times histogram
     */
    @GetMapping({"/histograms/clients"})
    public ResponseEntity<?> getHistogram(
        @RequestParam(value = "from") String from,
        @RequestParam(value = "to") String to
    ) throws IOException {

        Timerange timerange = Timerange.builder().type(TimeRangeType.absolute).from(from).to(to).build();

        Interval interval = Interval.builder()
            .type(IntervalType.timeunit)
            .timeunit(IntervalTimeunit.get(IntervalTimeunit.Unit.minutes, 1))
            .build();

        List<Series> seriesList = List.of(
            Series.builder().type(SeriesType.count).build(),
            Series.builder().type(SeriesType.avg).field("process_time").build()
        );

        List<SearchTypePivot> columnGroups = List.of(
            SearchTypePivot.builder().type(SearchTypePivotType.values).field("client_name").limit(5).build()
        );

        Histogram histogram = graylogSearchService.getHistogram(
            timerange,
            interval,
            GraylogQuery.builder()
                .field("message", "API_REQUEST_FINISHED"),
            seriesList,
            columnGroups
        );

        return new ResponseEntity<>(histogram, HttpStatus.OK);
    }
}
