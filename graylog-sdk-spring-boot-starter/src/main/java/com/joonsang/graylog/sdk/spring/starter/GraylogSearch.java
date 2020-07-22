package com.joonsang.graylog.sdk.spring.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joonsang.graylog.sdk.spring.starter.domain.MessageList;
import com.joonsang.graylog.sdk.spring.starter.domain.Page;
import com.joonsang.graylog.sdk.spring.starter.domain.SortConfig;
import com.joonsang.graylog.sdk.spring.starter.domain.Timerange;
import com.joonsang.graylog.sdk.spring.starter.search.Search;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Graylog Search
 * @author debugrammer
 * @since 2.0.0
 */
public class GraylogSearch {

    private final ObjectMapper objectMapper;

    private final Search search;

    public GraylogSearch(
        ObjectMapper objectMapper,
        Search search
    ) {

        this.objectMapper = objectMapper;
        this.search = search;
    }

    /**
     * Message list with paging.
     * @param streamIds Graylog Stream ID list
     * @param timerange time range object
     * @param searchQuery Graylog search query
     * @param pageSize size of each page
     * @param pageNo page number
     * @param sort sort config object
     * @param messageObject message object
     * @return List of message with paging
     * @throws IOException Graylog server failure
     * @since 2.0.0
     */
    public Page<?> getMessages(
        List<String> streamIds,
        Timerange timerange,
        String searchQuery,
        int pageSize,
        int pageNo,
        SortConfig sort,
        Class<?> messageObject
    ) throws IOException {

        int offset = (pageSize * pageNo) - pageSize;

        MessageList messageList = search.getMessages(timerange, searchQuery, pageSize, offset, sort, streamIds);

        List<Map<String, Map<String, ?>>> messageMapList = messageList.getMessages();

        return Page.builder()
            .pageNo(pageNo)
            .pageSize(pageSize)
            .list(
                messageMapList.stream()
                    .map(e -> objectMapper.convertValue(e.get("message"), messageObject))
                    .collect(Collectors.toList())
            )
            .totalCount(messageList.getTotalCount())
            .build();
    }

    public void sample(String query, List<String> streamIds) throws IOException {
        search.sample(query, streamIds);
    }
}
