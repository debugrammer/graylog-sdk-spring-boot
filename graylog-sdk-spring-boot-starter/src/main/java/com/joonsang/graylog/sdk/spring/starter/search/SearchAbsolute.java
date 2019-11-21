package com.joonsang.graylog.sdk.spring.starter.search;

import com.jayway.jsonpath.JsonPath;
import com.joonsang.graylog.sdk.spring.starter.GraylogRequest;
import com.joonsang.graylog.sdk.spring.starter.GraylogUtils;
import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogSdkProperties;
import com.joonsang.graylog.sdk.spring.starter.domain.*;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Search with absolute time range.
 * @author debugrammer
 * @since 1.0.0
 */
public class SearchAbsolute {

    private final GraylogRequest graylogRequest;

    private final GraylogSdkProperties graylogSdkProperties;

    public SearchAbsolute(
        GraylogRequest graylogRequest,
        GraylogSdkProperties graylogSdkProperties
    ) {

        this.graylogRequest = graylogRequest;
        this.graylogSdkProperties = graylogSdkProperties;
    }

    public List<Map<String, Map<String, ?>>> getMessages(
        String fields,
        String query,
        String from,
        String to,
        String filter
    ) throws IOException {

        HttpUrl httpUrl = graylogRequest.getHttpUrlBuilder()
            .addPathSegments("api/search/universal/absolute")
            .addQueryParameter("fields", fields)
            .addQueryParameter("query", query)
            .addQueryParameter("from", from)
            .addQueryParameter("to", to)
            .addQueryParameter("filter", filter)
            .build();

        String body = graylogRequest.httpGetRequest(httpUrl);

        @SuppressWarnings("unchecked")
        List<Map<String, Map<String, ?>>> messages = JsonPath.parse(body).read("$.messages", List.class);

        return messages;
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

    public Histogram getHistogram(
        String query,
        String interval,
        String from,
        String to,
        String filter
    ) throws IOException {

        HttpUrl httpUrl = graylogRequest.getHttpUrlBuilder()
            .addPathSegments("api/search/universal/absolute/histogram")
            .addQueryParameter("query", query)
            .addQueryParameter("interval", interval)
            .addQueryParameter("from", from)
            .addQueryParameter("to", to)
            .addQueryParameter("filter", filter)
            .build();

        String body = graylogRequest.httpGetRequest(httpUrl);

        @SuppressWarnings("unchecked")
        Map<String, Integer> resultMap = JsonPath.parse(body).read("$.results", Map.class);

        Map<Long, Integer> sortedResult = new TreeMap<>();

        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            sortedResult.put(Long.valueOf(entry.getKey()), entry.getValue());
        }

        List<HistogramData> results = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : sortedResult.entrySet()) {
            HistogramData histogramData = new HistogramData(
                GraylogUtils.convertTimestampToStringDate(
                    graylogSdkProperties.getTimezone(),
                    entry.getKey(),
                    interval
                ),
                entry.getValue()
            );

            results.add(histogramData);
        }

        return new Histogram(results);
    }

    public FieldHistogram getFieldHistogram(
        String field,
        String query,
        String interval,
        String from,
        String to,
        String filter
    ) throws IOException {

        HttpUrl httpUrl = graylogRequest.getHttpUrlBuilder()
            .addPathSegments("api/search/universal/absolute/fieldhistogram")
            .addQueryParameter("field", field)
            .addQueryParameter("query", query)
            .addQueryParameter("interval", interval)
            .addQueryParameter("from", from)
            .addQueryParameter("to", to)
            .addQueryParameter("filter", filter)
            .build();

        String body = graylogRequest.httpGetRequest(httpUrl);

        @SuppressWarnings("unchecked")
        Map<String, Map<String, ?>> resultMap = JsonPath.parse(body).read("$.results", Map.class);

        Map<Long, Map<String, ?>> sortedResult = new TreeMap<>();

        for (Map.Entry<String, Map<String, ?>> entry : resultMap.entrySet()) {
            sortedResult.put(Long.valueOf(entry.getKey()), entry.getValue());
        }

        List<FieldHistogramData> results = new ArrayList<>();

        for (Map.Entry<Long, Map<String, ?>> entry : sortedResult.entrySet()) {
            FieldHistogramData fieldHistogramData = new FieldHistogramData();
            fieldHistogramData.setLabel(
                GraylogUtils.convertTimestampToStringDate(
                    graylogSdkProperties.getTimezone(),
                    entry.getKey(),
                    interval
                )
            );
            fieldHistogramData.setTotalCount((Integer) entry.getValue().get("total_count"));
            fieldHistogramData.setCount((Integer) entry.getValue().get("count"));
            fieldHistogramData.setMin(
                entry.getValue().get("min") instanceof BigDecimal
                    ? ((BigDecimal) entry.getValue().get("min")).doubleValue()
                    : (Double) entry.getValue().get("min")
            );
            fieldHistogramData.setMax(
                entry.getValue().get("max") instanceof BigDecimal
                    ? ((BigDecimal) entry.getValue().get("max")).doubleValue()
                    : (Double) entry.getValue().get("max")
            );
            fieldHistogramData.setTotal(
                entry.getValue().get("total") instanceof BigDecimal
                    ? ((BigDecimal) entry.getValue().get("total")).doubleValue()
                    : (Double) entry.getValue().get("total")
            );
            fieldHistogramData.setMean(
                entry.getValue().get("mean") instanceof BigDecimal
                    ? ((BigDecimal) entry.getValue().get("mean")).doubleValue()
                    : (Double) entry.getValue().get("mean")
            );
            fieldHistogramData.setCardinality((Integer) entry.getValue().get("cardinality"));

            results.add(fieldHistogramData);
        }

        return new FieldHistogram(results);
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
            sortedResult = termsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(
                    Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e2,
                        LinkedHashMap::new
                    )
                );
        } else {
            sortedResult = termsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(
                    Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e2,
                        LinkedHashMap::new
                    )
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
