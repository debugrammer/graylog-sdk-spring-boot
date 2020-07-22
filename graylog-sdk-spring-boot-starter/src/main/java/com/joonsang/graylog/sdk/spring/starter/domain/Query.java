package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.uuid.Generators;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

/**
 * Query
 * @author debugrammer
 * @since 2.0.0
 */
@Builder
@Getter
public class Query {

    @Builder.Default
    private final String id = Generators.randomBasedGenerator().generate().toString();

    private final SearchQuery query;

    private final Timerange timerange;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Filter filter;

    @Singular
    @JsonProperty("search_types")
    private final List<SearchType> searchTypes;
}
