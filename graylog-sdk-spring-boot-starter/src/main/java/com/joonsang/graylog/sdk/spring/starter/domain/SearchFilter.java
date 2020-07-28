package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * Search Filter
 * @author debugrammer
 * @since 2.0.0
 */
@Builder
@Getter
public class SearchFilter {

    @Builder.Default
    private final String type = "stream";

    private final String id;
}
