package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joonsang.graylog.sdk.spring.starter.constant.SortConfigDirection;
import com.joonsang.graylog.sdk.spring.starter.constant.SortConfigOrder;
import com.joonsang.graylog.sdk.spring.starter.constant.SortConfigType;
import lombok.Builder;
import lombok.Getter;

/**
 * Sort Config
 * @author debugrammer
 * @since 2.0.0
 */
@Builder
@Getter
public class SortConfig {

    /**
     * Work with SearchTypeType.pivot type of {@link SearchType}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final SortConfigType type;

    /**
     * Work with SearchTypeType.pivot/SearchTypeType.messages types of {@link SearchType}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final SortConfigDirection direction;

    private final String field;

    /**
     * Work with SearchTypeType.messages type of {@link SearchType}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final SortConfigOrder order;
}
