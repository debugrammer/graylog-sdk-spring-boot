package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchFilter {

    @Builder.Default
    private final String type = "stream";

    private final String id;
}
