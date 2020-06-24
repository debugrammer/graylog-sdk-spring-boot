package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * Message List Domain
 * @author debugrammer
 * @since 1.2.0
 */
@Getter
@Setter
@ToString
public class MessageList {

    @Builder
    public MessageList(List<Map<String, Map<String, ?>>> messages, Integer totalCount) {
        this.messages = messages;
        this.totalCount = totalCount;
    }

    private List<Map<String, Map<String, ?>>> messages;

    private Integer totalCount;
}
