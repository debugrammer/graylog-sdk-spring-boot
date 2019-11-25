# Graylog SDK Spring Boot Starter
> Graylog SDK Spring Boot Starter to communicate with the [Graylog REST API](https://docs.graylog.org/en/latest/pages/configuration/rest_api.html)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.joonsang.graylog/graylog-sdk-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.joonsang.graylog/graylog-sdk-spring-boot-starter)
[![Javadoc](https://javadoc-badge.appspot.com/com.joonsang.graylog/graylog-sdk-spring-boot-starter.svg?label=javadoc)](https://javadoc-badge.appspot.com/com.joonsang.graylog/graylog-sdk-spring-boot-starter)

## Getting Started
Graylog SDK Spring Boot Starter is available at the Central Maven Repository.

**Maven**
```
<dependency>
  <groupId>com.joonsang.graylog</groupId>
  <artifactId>graylog-sdk-spring-boot-starter</artifactId>
  <version>1.0.1</version>
</dependency>
```

**Gradle**
```
implementation group: 'com.joonsang.graylog', name: 'graylog-sdk-spring-boot-starter', version: '1.0.1'
```

## Usage
Configure environment variables for Graylog SDK with `application.properties` or `application.yaml`:
```
# Graylog SDK Settings
graylog.sdk.timezone={your_graylog_server_timezone}

# Graylog API Settings
graylog.sdk.api.scheme={your_graylog_api_scheme}
graylog.sdk.api.host={your_graylog_api_host}
graylog.sdk.api.port={your_graylog_api_port}
graylog.sdk.api.credentials={your_graylog_api_credentials}
```

Then inject `GraylogSearch` bean in your project:
```
private final GraylogSearch graylogSearch;

public YourClassName(GraylogSearch graylogSearch) {
    this.graylogSearch = graylogSearch;
}
```

## Request Graylog REST APIs
Currently, only Search APIs are supported.

### 1. Search

#### 1.1. Messages
Message search with absolute time range.

Create JavaBean class specifying your Graylog message fields.
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
    "your-graylog-stream-id",
    from,
    to,
    "your graylog search query",
    YourMessageObject.class
);
```

#### 1.2. Statistics
Field statistics for a query using an absolute time range.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

Statistics statistics = graylogSearch.getStatistics(
    "your-graylog-stream-id",
    "field_name",
    from,
    to,
    "your graylog search query"
);
```

#### 1.3. Histogram
Datetime histogram of a query using an absolute time range.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

Histogram histogram = graylogSearch.getHistogram(
    "your-graylog-stream-id",
    TimeUnit.HOUR,
    from,
    to,
    "your graylog search query"
);
```

#### 1.4. Field Histogram
Field value histogram of a query using an absolute time range.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

FieldHistogram fieldHistogram = graylogSearch.getFieldHistogram(
    "your-graylog-stream-id",
    "field_name",
    TimeUnit.HOUR,
    from,
    to,
    "your graylog search query"
);
```

#### 1.5. Terms
Most common field terms of a query using an absolute time range.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

Terms terms = graylogSearch.getTerms(
    "your-graylog-stream-id",
    "field_name",
    "field_name_to_stack",
    5,
    from,
    to,
    false,
    false,
    "your graylog search query"
);
```

## Code Examples
* [graylog-sdk-spring-boot-samples](https://github.com/debugrammer/graylog-sdk-spring-boot/tree/master/graylog-sdk-spring-boot-samples) in this repository contains the project that show you sample API implementations using Graylog SDK with Spring Boot.
