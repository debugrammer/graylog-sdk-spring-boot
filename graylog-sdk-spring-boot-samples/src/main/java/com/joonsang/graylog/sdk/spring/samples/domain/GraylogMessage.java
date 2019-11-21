package com.joonsang.graylog.sdk.spring.samples.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraylogMessage {

    @JsonProperty("request_id")
    private String requestId;

    private String message;

    private String source;

    private String timestamp;
}
