package com.joonsang.graylog.sdk.spring.starter.search;

import com.jayway.jsonpath.JsonPath;
import com.joonsang.graylog.sdk.spring.starter.GraylogRequest;
import com.joonsang.graylog.sdk.spring.starter.GraylogUtils;
import com.joonsang.graylog.sdk.spring.starter.domain.Statistics;
import com.joonsang.graylog.sdk.spring.starter.domain.Terms;
import com.joonsang.graylog.sdk.spring.starter.domain.TermsData;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Search Absolute
 * @author debugrammer
 * @since 1.0.0
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

    public Terms getTerms(
        String field,
        String stackedFields,
        String query,
        String from,
        String to,
        String filter,
        int size,
        boolean reverseOrder,
        boolean topValuesOnly
    ) throws IOException {

        HttpUrl httpUrl = graylogRequest.getHttpUrlBuilder()
            .addPathSegments("api/search/universal/absolute/terms")
            .addQueryParameter("field", field)
            .addQueryParameter("stacked_fields", stackedFields)
            .addQueryParameter("query", query)
            .addQueryParameter("from", from)
            .addQueryParameter("to", to)
            .addQueryParameter("size", String.valueOf(size))
            .addQueryParameter("filter", filter)
            .addQueryParameter("order", reverseOrder ? field + ":asc" : StringUtils.EMPTY)
            .build();

        String body = graylogRequest.httpGetRequest(httpUrl);

        @SuppressWarnings("unchecked")
        Map<String, Integer> termsMap = JsonPath.parse(body).read("$.terms", Map.class);

        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, String>>> termsMappingMap = JsonPath.parse(body).read("$.terms_mapping", Map.class);

        int missing = JsonPath.parse(body).read("$.missing", Integer.class);
        int other = JsonPath.parse(body).read("$.other", Integer.class);
        int total = JsonPath.parse(body).read("$.total", Integer.class);

        total = total - missing;

        if (topValuesOnly) {
            total = total - other;
        }

        Map<String, Integer> sortedResult;

        if (reverseOrder) {
            sortedResult = termsMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)
                );
        } else {
            sortedResult = termsMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)
                );
        }

        List<TermsData> results = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : sortedResult.entrySet()) {
            List<String> labels = new ArrayList<>();

            for (Map<String, String> termsMapping : termsMappingMap.get(entry.getKey())) {
                labels.add(termsMapping.get("value"));
            }

            TermsData termsData = new TermsData(
                labels,
                entry.getValue(),
                (double) entry.getValue() / (double) total
            );

            results.add(termsData);
        }

        if (!topValuesOnly) {
            TermsData termsData = new TermsData(
                List.of("Other"),
                other,
                (double) other / (double) total
            );

            results.add(termsData);
        }

        return new Terms(results);
    }
}
