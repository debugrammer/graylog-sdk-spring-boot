package com.joonsang.graylog.sdk.spring.starter;

import com.jayway.jsonpath.JsonPath;

/**
 * Graylog SDK Utils
 * @author debugrammer
 * @since 1.0.0
 */
public class GraylogUtils {

    public static Double getDoubleFromJsonPath(String body, String path) {
        try {
            return JsonPath.parse(body).read(path, Double.class);
        } catch (NumberFormatException nfe) {
            return Double.NaN;
        }
    }
}
