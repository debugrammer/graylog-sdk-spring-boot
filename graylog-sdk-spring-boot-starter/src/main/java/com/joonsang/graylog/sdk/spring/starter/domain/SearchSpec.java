package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Search Specification
 * @author debugrammer
 * @since 2.0.0
 */
@Builder
@Getter
public class SearchSpec {

    @Builder.Default
    private final String id = new ObjectId().toString();

    @Singular
    private final List<Query> queries;

    @Singular
    private final List<Parameter> parameters;
}
