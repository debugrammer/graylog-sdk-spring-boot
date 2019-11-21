package com.joonsang.graylog.sdk.spring.samples.controller;

import com.joonsang.graylog.GraylogQuery;
import com.joonsang.graylog.sdk.spring.samples.domain.GraylogMessage;
import com.joonsang.graylog.sdk.spring.samples.domain.TwoStatistics;
import com.joonsang.graylog.sdk.spring.samples.service.GraylogSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

@RestController
@RequestMapping({"/v1/search"})
public class GraylogSearchController {

    private final GraylogSearchService graylogSearchService;

    public GraylogSearchController(GraylogSearchService graylogSearchService) {
        this.graylogSearchService = graylogSearchService;
    }

    /**
     * Message.
     * Get message by API request ID.
     */
    @GetMapping({"/messages/{requestId}"})
    public ResponseEntity<?> getMessageByRequestId(
        @PathVariable("requestId") String requestId
    ) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        LocalDateTime fromDateTime = LocalDateTime.now().minusMonths(3);
        LocalDateTime toDateTime = LocalDateTime.now();

        GraylogMessage message = graylogSearchService.getMessage(
            fromDateTime,
            toDateTime,
            GraylogQuery.builder()
                .field("request_id", requestId)
        );

        if (message == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Statistics.
     * Generates two statistics by sources' process times are between 0ms and 500ms.
     * One has the stats from today to yesterday, the another has last 7 days stats.
     */
    @GetMapping({"/statistics/comparisons/period/sources"})
    public ResponseEntity<?> getCompareSourceProcessTimeStats() throws IOException {
        LocalDateTime firstDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime secondDateTime = LocalDateTime.now().minusDays(7);

        TwoStatistics twoStats = graylogSearchService.getTwoStats(
            "source",
            firstDateTime,
            secondDateTime,
            GraylogQuery.builder()
                .field("message", "API_REQUEST_FINISHED")
                .and().range("process_time", "[", 0, 500, "]")
        );

        return new ResponseEntity<>(twoStats, HttpStatus.OK);
    }
}
