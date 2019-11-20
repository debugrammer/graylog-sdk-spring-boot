package com.joonsang.graylog.sdk.spring.starter.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Graylog SDK Properties
 * @author debugrammer
 * @version 1.0
 * @since 2019-11-18
 */
@Getter
@Setter
@ConfigurationProperties("graylog.api")
public class GraylogProperties {

    private String scheme;

    private String host;

    private Integer port;

    private String credentials;
}
