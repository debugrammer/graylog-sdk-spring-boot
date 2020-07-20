package com.joonsang.graylog.sdk.spring.starter.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import com.joonsang.graylog.sdk.spring.starter.GraylogRequest;
import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogSdkProperties;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;

/**
 * Search
 * @author debugrammer
 * @since 2.0.0
 */
public class Search {

    private static final MediaType CONTENT_TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    private final GraylogRequest graylogRequest;

    private final GraylogSdkProperties graylogSdkProperties;

    private final ObjectMapper objectMapper;

    public Search(
        GraylogRequest graylogRequest,
        GraylogSdkProperties graylogSdkProperties,
        ObjectMapper objectMapper
    ) {

        this.graylogRequest = graylogRequest;
        this.graylogSdkProperties = graylogSdkProperties;
        this.objectMapper = objectMapper;
    }

    public void sample(List<String> streamIds) throws IOException {
        ObjectId id = new ObjectId();

        RandomBasedGenerator generator = Generators.randomBasedGenerator();

        String requestJson = "{\n" +
                "  \"id\": \"" + id + "\",\n" +
                "  \"queries\": [\n" +
                "    {\n" +
                "      \"id\": \"" + generator.generate().toString() + "\",\n" +
                "      \"query\": {\n" +
                "        \"type\": \"elasticsearch\",\n" +
                "        \"query_string\": \"\"\n" +
                "      },\n" +
                "      \"timerange\": {\n" +
                "        \"type\": \"relative\",\n" +
                "        \"range\": 300\n" +
                "      },\n" +
                "      \"search_types\": [\n" +
                "        {\n" +
                "          \"name\": \"chart\",\n" +
                "          \"streams\": [\"" + streamIds.get(0) + "\"],\n" +
                "          \"series\": [\n" +
                "            {\n" +
                "              \"id\": \"count()\",\n" +
                "              \"type\": \"count\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"rollup\": true,\n" +
                "          \"row_groups\": [\n" +
                "            {\n" +
                "              \"type\": \"values\",\n" +
                "              \"field\": \"client_name\",\n" +
                "              \"limit\": 15\n" +
                "            }\n" +
                "          ],\n" +
                "          \"type\": \"pivot\",\n" +
                "          \"id\": \"" + generator.generate().toString() + "\",\n" +
                "          \"column_groups\": [],\n" +
                "          \"sort\": []\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"parameters\": []\n" +
                "}";

//        String requestJson = objectMapper.writeValueAsString(webhookRequest);
        RequestBody jsonBody = RequestBody.create(requestJson, CONTENT_TYPE_JSON);

        HttpUrl httpUrl = graylogRequest.getHttpUrlBuilder()
            .addPathSegments("api/views/search/sync")
            .addQueryParameter("timeout", "60000")
            .build();

        String body = graylogRequest.httpPostRequest(httpUrl, jsonBody);

        System.out.println(body);
    }
}
