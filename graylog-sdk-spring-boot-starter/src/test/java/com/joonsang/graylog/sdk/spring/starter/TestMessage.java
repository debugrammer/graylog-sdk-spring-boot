package com.joonsang.graylog.sdk.spring.starter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
class TestMessage {

    private String message;

    private String source;

    private String timestamp;
}
