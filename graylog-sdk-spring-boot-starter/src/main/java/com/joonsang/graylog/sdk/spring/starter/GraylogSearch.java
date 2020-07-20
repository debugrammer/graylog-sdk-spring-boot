package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.search.Search;

import java.io.IOException;
import java.util.List;

/**
 * Graylog Search
 * @author debugrammer
 * @since 2.0.0
 */
public class GraylogSearch {

    private final Search search;

    public GraylogSearch(Search search) {
        this.search = search;
    }

    public void sample(List<String> streamIds) throws IOException {
        search.sample(streamIds);
    }
}
