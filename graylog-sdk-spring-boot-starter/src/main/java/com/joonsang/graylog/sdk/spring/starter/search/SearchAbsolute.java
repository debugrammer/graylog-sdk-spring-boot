package com.joonsang.graylog.sdk.spring.starter.search;

import com.jayway.jsonpath.JsonPath;
import com.joonsang.graylog.sdk.spring.starter.GraylogRequest;
import com.joonsang.graylog.sdk.spring.starter.GraylogUtils;
import com.joonsang.graylog.sdk.spring.starter.domain.Statistics;
import okhttp3.HttpUrl;

import java.io.IOException;

/**
 * Search Absolute
 * @author debugrammer
 * @version 1.0
 * @since 2019-11-20
 */
public class SearchAbsolute {

    private final GraylogRequest graylogRequest;

    public SearchAbsolute(GraylogRequest graylogRequest) {
        this.graylogRequest = graylogRequest;
    }

    public Statistics getStatistics(
        String field,
        String query,
        String from,
        String to,
        String filter
    ) throws IOException {

        HttpUrl httpUrl = graylogRequest.getHttpUrlBuilder()
            .addPathSegments("api/search/universal/absolute/stats")
            .addQueryParameter("field", field)
            .addQueryParameter("query", query)
            .addQueryParameter("from", from)
            .addQueryParameter("to", to)
            .addQueryParameter("filter", filter)
            .build();

        String body = graylogRequest.httpGetRequest(httpUrl);

        Statistics statistics = new Statistics();
        statistics.setCount(JsonPath.parse(body).read("$.count", Integer.class));
        statistics.setSum(GraylogUtils.getDoubleFromJsonPath(body, "$.sum"));
        statistics.setSumOfSquares(GraylogUtils.getDoubleFromJsonPath(body, "$.sum_of_squares"));
        statistics.setMean(GraylogUtils.getDoubleFromJsonPath(body, "$.mean"));
        statistics.setMin(GraylogUtils.getDoubleFromJsonPath(body, "$.min"));
        statistics.setMax(GraylogUtils.getDoubleFromJsonPath(body, "$.max"));
        statistics.setVariance(GraylogUtils.getDoubleFromJsonPath(body, "$.variance"));
        statistics.setStdDeviation(GraylogUtils.getDoubleFromJsonPath(body, "$.std_deviation"));
        statistics.setCardinality(JsonPath.parse(body).read("$.cardinality", Integer.class));

        return statistics;
    }
}
