package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.uuid.Generators;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class SearchType {

    @Builder.Default
    private final String id = Generators.randomBasedGenerator().generate().toString();

    private final String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final SearchQuery query;

    private final String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Timerange timerange;

    @Builder.Default
    private final List<String> streams = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String filter;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<SortConfig> sort;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<Series> series;

    @Singular
    @JsonProperty("column_groups")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<SearchTypePivot> columnGroups;

    @Singular
    @JsonProperty("row_groups")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<SearchTypePivot> rowGroups;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Boolean rollup;

    @Singular
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Decorator> decorators;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer limit;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer offset;
}
