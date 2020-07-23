package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.uuid.Generators;
import com.joonsang.graylog.sdk.spring.starter.constant.SearchTypeType;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

/**
 * Search Type
 * @author debugrammer
 * @since 2.0.0
 */
@Builder
@Getter
public class SearchType {

    @Builder.Default
    private final String id = Generators.randomBasedGenerator().generate().toString();

    private final String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final SearchQuery query;

    private final SearchTypeType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Timerange timerange;

    @Builder.Default
    private final List<String> streams = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String filter;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<SortConfig> sort;

    /**
     * Work with SearchTypeType.pivot type of {@link SearchType}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<Series> series;

    /**
     * Work with SearchTypeType.pivot type of {@link SearchType}
     */
    @JsonProperty("column_groups")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<SearchTypePivot> columnGroups;

    /**
     * Work with SearchTypeType.pivot type of {@link SearchType}
     */
    @JsonProperty("row_groups")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<SearchTypePivot> rowGroups;

    /**
     * Work with SearchTypeType.pivot type of {@link SearchType}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Boolean rollup;

    /**
     * Work with SearchTypeType.messages type of {@link SearchType}
     */
    @Singular
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Decorator> decorators;

    /**
     * Work with SearchTypeType.messages type of {@link SearchType}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer limit;

    /**
     * Work with SearchTypeType.messages type of {@link SearchType}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer offset;
}
