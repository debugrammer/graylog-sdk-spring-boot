package com.joonsang.graylog.sdk.spring.starter.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Graylog API Properties
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("graylog.sdk.api")
public class GraylogApiProperties {

    private String scheme;

    private String host;

    private Integer port;

    private String credentials;
}
