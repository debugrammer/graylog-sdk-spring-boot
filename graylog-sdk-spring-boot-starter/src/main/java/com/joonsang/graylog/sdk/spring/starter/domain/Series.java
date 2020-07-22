package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joonsang.graylog.sdk.spring.starter.constant.SeriesType;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Series
 * @author debugrammer
 * @since 2.0.0
 */
@Getter
public class Series {

    @Builder
    public Series(String id, SeriesType type, String field, Float percentile) {
        this.type = type;
        this.field = field;
        this.percentile = percentile;
        this.id = initId(id);
    }

    private final String id;

    private final SeriesType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String field;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Float percentile;

    private String initId(String id) {
        if (!StringUtils.isEmpty(id)) {
            return id;
        }

        if (StringUtils.isEmpty(this.field)) {
            return this.type + "()";
        }

        if (this.type == SeriesType.percentile) {
            return this.type + "(" + this.field + "," + this.percentile + ")";
        }

        return this.type + "(" + this.field + ")";
    }
}
