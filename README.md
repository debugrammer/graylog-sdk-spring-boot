# Graylog SDK Spring Boot Starter
> Graylog SDK Spring Boot Starter to communicate with the [Graylog REST API](https://docs.graylog.org/en/latest/pages/configuration/rest_api.html)

[![Build Status](https://travis-ci.org/debugrammer/graylog-sdk-spring-boot.svg?branch=master)](https://travis-ci.org/debugrammer/graylog-sdk-spring-boot)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.joonsang.graylog/graylog-sdk-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.joonsang.graylog/graylog-sdk-spring-boot-starter)
[![Javadoc](https://javadoc-badge.appspot.com/com.joonsang.graylog/graylog-sdk-spring-boot-starter.svg?label=javadoc)](https://javadoc-badge.appspot.com/com.joonsang.graylog/graylog-sdk-spring-boot-starter)

## Getting Started
Graylog SDK Spring Boot Starter is available at the Central Maven Repository.

**Maven**
```
<dependency>
  <groupId>com.joonsang.graylog</groupId>
  <artifactId>graylog-sdk-spring-boot-starter</artifactId>
  <version>1.1.7</version>
</dependency>
```

**Gradle**
```
implementation group: 'com.joonsang.graylog', name: 'graylog-sdk-spring-boot-starter', version: '1.1.7'
```

## Usage
Configure environment variables for Graylog SDK with `application.properties`:
```
# Graylog API Settings
graylog.sdk.api.scheme=http
graylog.sdk.api.host=localhost
graylog.sdk.api.port=9000
graylog.sdk.api.credentials=base64({graylog_access_token}:token)
graylog.sdk.timezone=US/Eastern
```

or `application.yml`:
```
# Graylog API Settings
graylog:
  sdk:
    api:
      scheme: http
      host: localhost
      port: 9000
      credentials: base64({graylog_access_token}:token)
    timezone: US/Eastern
```

Then inject `GraylogSearch` bean in your project:
```
private final GraylogSearch graylogSearch;

public YourClassName(GraylogSearch graylogSearch) {
    this.graylogSearch = graylogSearch;
}
```

## Request Graylog REST APIs
> Old APIs in `Search` section were moved to `Legacy/Search` section from [Graylog 3.2](https://www.graylog.org/post/announcing-graylog-3-2)

Currently, only `Legacy/Search/Absolute` APIs are supported.

### 1. Legacy Search Absolute

#### 1.1. Messages
Message search with absolute time range.

Create POJO class specifying your Graylog message fields.
```
public class YourMessageObject {

    private String message;
    
    private String source;

    private String timestamp;

    // getters and setters
}
```

Graylog SDK will return the list of message object as you specified.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

@SuppressWarnings("unchecked")
List<YourMessageObject> messages = (List<YourMessageObject>) graylogSearch.getMessages(
    "graylog_stream_id",
    from,
    to,
    "request_id:AQZ4mfVGVqWKD38XZU7aVG",
    YourMessageObject.class
);
```

#### 1.2. Statistics
Field statistics for a query using an absolute time range.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

Statistics statistics = graylogSearch.getStatistics(
    "graylog_stream_id",
    "field_name",
    from,
    to,
    "process_time:[0 TO 500]"
);
```

#### 1.3. Histogram
Datetime histogram of a query using an absolute time range.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

Histogram histogram = graylogSearch.getHistogram(
    "graylog_stream_id",
    TimeUnit.HOUR,
    from,
    to,
    "process_time:[0 TO 500]"
);
```

#### 1.4. Field Histogram
Field value histogram of a query using an absolute time range.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

FieldHistogram fieldHistogram = graylogSearch.getFieldHistogram(
    "graylog_stream_id",
    "field_name",
    TimeUnit.HOUR,
    from,
    to,
    "source:example.org"
);
```

#### 1.5. Terms
Most common field terms of a query using an absolute time range.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

Terms terms = graylogSearch.getTerms(
    "graylog_stream_id",
    "field_name",
    "field_name_to_stack",
    5,
    from,
    to,
    false,
    false,
    "source:example.org"
);
```

## Code Examples
* [graylog-sdk-spring-boot-samples](https://github.com/debugrammer/graylog-sdk-spring-boot/tree/master/graylog-sdk-spring-boot-samples) in this repository contains the project that show you sample API implementations using Graylog SDK with Spring Boot.
    * Check out [Graylog Query Builder](https://github.com/debugrammer/graylog-query-builder) if you are looking for query builder for Graylog search query.
