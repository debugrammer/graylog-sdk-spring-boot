package com.joonsang.graylog.sdk.spring.starter;

import com.joonsang.graylog.sdk.spring.starter.autoconfigure.GraylogApiProperties;
import com.joonsang.graylog.sdk.spring.starter.exception.GraylogServerException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Objects;

/**
 * Graylog REST API Request
 * @author debugrammer
 * @since 1.0.0
 */
public class GraylogRequest {

    private final OkHttpClient okHttpClient;

    private final GraylogApiProperties graylogApiProperties;

    public GraylogRequest(
        OkHttpClient okHttpClient,
        GraylogApiProperties graylogApiProperties
    ) {

        this.okHttpClient = okHttpClient;
        this.graylogApiProperties = graylogApiProperties;
    }

    /**
     * HTTP GET request.
     * @param httpUrl OkHttp HttpUrl object
     * @return Response from Graylog
     * @throws IOException Graylog server failure
     * @since 1.0.0
     */
    public String httpGetRequest(HttpUrl httpUrl) throws IOException {
        Request request = new Request.Builder()
            .url(httpUrl)
            .get()
            .build();

        Response response = okHttpClient.newCall(request).execute();

        validateResponse(response);

        return Objects.requireNonNull(response.body()).string();
    }

    /**
     * Validate Graylog server response.
     * @param response OkHttp Response object
     * @throws IOException Graylog server failure
     * @since 1.1.0
     */
    public void validateResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            String message;

            String errorBody = Objects.isNull(response.body())
                ? StringUtils.EMPTY
                : Objects.requireNonNull(response.body()).string();

            HttpStatus httpStatus = HttpStatus.valueOf(response.code());

            switch (httpStatus) {
                case UNAUTHORIZED:
                    message = "Invalid Graylog server credentials.";
                    break;
                case BAD_REQUEST:
                    message = StringUtils.isEmpty(errorBody)
                        ? "Bad request."
                        : "Bad request: " + errorBody;
                    break;
                default:
                    message = StringUtils.isEmpty(errorBody)
                        ? "Graylog server error."
                        : "Graylog server error: " + errorBody;
            }

            throw new GraylogServerException(message);
        }

        if (Objects.isNull(response.body())) {
            throw new GraylogServerException("Graylog server responded empty HTTP response body.");
        }
    }

    /**
     * Get prebuilt OkHttp HttpUrl.Builder object.
     * @return Prebuilt OkHttp HttpUrl.Builder object
     * @since 1.0.0
     */
    public HttpUrl.Builder getHttpUrlBuilder() {
        return new HttpUrl.Builder()
            .scheme(graylogApiProperties.getScheme())
            .host(graylogApiProperties.getHost())
            .port(graylogApiProperties.getPort());
    }
}
