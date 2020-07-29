package com.joonsang.graylog.sdk.spring.samples.controller;

import com.joonsang.graylog.GraylogQuery;
import com.joonsang.graylog.sdk.spring.samples.domain.GraylogMessage;
import com.joonsang.graylog.sdk.spring.samples.service.GraylogSearchService;
import com.joonsang.graylog.sdk.spring.starter.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping({"/v2/search"})
public class GraylogSearchController {

    private final GraylogSearchService graylogSearchService;

    public GraylogSearchController(GraylogSearchService graylogSearchService) {
        this.graylogSearchService = graylogSearchService;
    }

    /**
     * Messages.
     * Get successful messages with paging.
     */
    @GetMapping({"/messages"})
    public ResponseEntity<?> getSuccessfulMessages(
        @RequestParam(value = "page_no", defaultValue = "1") int pageNo,
        @RequestParam(value = "page_size", defaultValue = "10") int pageSize
    ) throws IOException {

        Page<GraylogMessage> messages = graylogSearchService.getMessages(
            "2020-07-28T09:00:00Z",
            "2020-07-28T10:00:00Z",
            GraylogQuery.builder()
                .field("message", "API_REQUEST_FINISHED"),
            pageSize,
            pageNo
        );

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
