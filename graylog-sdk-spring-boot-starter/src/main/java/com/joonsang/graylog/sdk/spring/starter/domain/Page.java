package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Page Domain
 * @author debugrammer
 * @since 1.2.0
 */
@Setter
@NoArgsConstructor
@ToString
public class Page<E> {

    @Builder
    public Page(int pageNo, int pageSize, int totalCount, List<E> list) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.list = list;
    }

    @Getter
    @JsonProperty("page_no")
    private Integer pageNo;

    @Getter
    @JsonProperty("page_size")
    private Integer pageSize;

    @Getter
    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("total_page_count")
    private Integer totalPageCount;

    @Getter
    @JsonProperty("list")
    private List<E> list;

    public double getTotalPageCount() {
        return (int) Math.ceil((double) totalCount / pageSize);
    }
}
