package com.joonsang.graylog.sdk.spring.starter;

import com.jayway.jsonpath.JsonPath;

/**
 * Graylog SDK Utils
 * @author debugrammer
 * @version 1.0
 * @since 2019-11-20
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
