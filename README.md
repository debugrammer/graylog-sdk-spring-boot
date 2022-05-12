# Graylog SDK Spring Boot Starter
> Graylog SDK Spring Boot Starter to communicate with the [Graylog REST API](https://docs.graylog.org/en/latest/pages/configuration/rest_api.html)

![build](https://github.com/debugrammer/graylog-sdk-spring-boot/actions/workflows/build.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.joonsang.graylog/graylog-sdk-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.joonsang.graylog/graylog-sdk-spring-boot-starter)
[![javadoc](https://javadoc.io/badge2/com.joonsang.graylog/graylog-sdk-spring-boot-starter/javadoc.svg)](https://javadoc.io/doc/com.joonsang.graylog/graylog-sdk-spring-boot-starter)

## Getting Started
Graylog SDK Spring Boot Starter is available at the Central Maven Repository.

**Maven**
```
<dependency>
  <groupId>com.joonsang.graylog</groupId>
  <artifactId>graylog-sdk-spring-boot-starter</artifactId>
  <version>2.0.1</version>
</dependency>
```

**Gradle**
```
implementation group: 'com.joonsang.graylog', name: 'graylog-sdk-spring-boot-starter', version: '2.0.1'
```

## Troubleshooting
* Exception occurred: `java.lang.NoSuchMethodError: 'okhttp3.RequestBody okhttp3.RequestBody.create(java.lang.String, okhttp3.MediaType)'`
  * There is an OkHttp3 version under 4.x somewhere in your project dependencies.
  * Add OkHttp3 4.x in your dependency like below.
    * `implementation group: 'com.squareup.okhttp3', name: 'okhttp', version: '4.x.x'`
  * https://github.com/debugrammer/graylog-sdk-spring-boot/issues/3

## Graylog API Credentials
First create user access token on your Graylog web interface for accessing Graylog APIs.

Refer Graylog documentation: [Creating and using Access Token](https://docs.graylog.org/en/latest/pages/configuration/rest_api.html#creating-and-using-access-token)

## Graylog 3.2 Search
### Usage
Configure environment variables for Graylog SDK with `application.properties`:
```
# Graylog API Settings
graylog.sdk.api.scheme=http
graylog.sdk.api.host=localhost
graylog.sdk.api.port=9000
graylog.sdk.api.credentials=base64({graylog_access_token}:token)
graylog.sdk.api.timeout=60000
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
      timeout: 60000
```

Then inject `GraylogSearch` bean in your project:
```
private final GraylogSearch graylogSearch;

public YourClassName(GraylogSearch graylogSearch) {
    this.graylogSearch = graylogSearch;
}
```

### Request Graylog REST APIs

### 1. Search

#### 1.1. Messages
Message search with time range.

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
String from = "2020-07-30T00:00:00Z";
String to = "2020-07-31T00:00:00Z";

Timerange timerange = Timerange.builder()
    .type(TimeRangeType.absolute)
    .from(from)
    .to(to)
    .build();

SortConfig sort = SortConfig.builder()
    .field("timestamp")
    .order(SortConfigOrder.DESC)
    .build();

int pageSize = 20;
int pageNo = 1;

@SuppressWarnings("unchecked")
Page<YourMessageObject> messages = (Page<YourMessageObject>) graylogSearch.getMessages(
    List.of("graylog_stream_id"),
    timerange,
    "source:example.org",
    pageSize,
    pageNo,
    sort,
    YourMessageObject.class
);
```

#### 1.2. Statistics
Statistics for a query using a time range.
```
String from = "2020-07-30T00:00:00Z";
String to = "2020-07-31T00:00:00Z";

Timerange timerange = Timerange.builder()
    .type(TimeRangeType.absolute)
    .from(from)
    .to(to)
    .build();

List<Series> seriesList = List.of(
    Series.builder().type(SeriesType.avg).field("process_time").build(),
    Series.builder().type(SeriesType.count).field("process_time").build(),
    Series.builder().type(SeriesType.min).field("process_time").build(),
    Series.builder().type(SeriesType.max).field("process_time").build(),
    Series.builder().type(SeriesType.percentile).percentile(95.0f).field("process_time").build(),
    Series.builder().type(SeriesType.percentile).percentile(99.0f).field("process_time").build(),
    Series.builder().type(SeriesType.count).build()
);

List<Statistics> statistics = graylogSearch.getStatistics(
    List.of("graylog_stream_id"),
    timerange,
    "source:example.org",
    seriesList
);
```

#### 1.3. Histogram
Datetime histogram of a query using a time range.
```
String from = "2020-07-30T00:00:00Z";
String to = "2020-07-31T00:00:00Z";

Timerange timerange = Timerange.builder()
    .type(TimeRangeType.absolute)
    .from(from)
    .to(to)
    .build();

Interval interval = Interval.builder()
    .type(IntervalType.timeunit)
    .timeunit(IntervalTimeunit.get(IntervalTimeunit.Unit.minutes, 1))
    .build();

List<Series> seriesList = List.of(
    Series.builder().type(SeriesType.count).build(),
    Series.builder().type(SeriesType.avg).field("process_time").build()
);

List<SearchTypePivot> columnGroups = List.of(
    SearchTypePivot.builder().type(SearchTypePivotType.values).field("field_name").limit(5).build()
);

Histogram histogram = graylogSearch.getHistogram(
    List.of("graylog_stream_id"),
    timerange,
    interval,
    "source:example.org",
    seriesList,
    columnGroups
);
```

#### 1.4. Terms
> Differences between Quick Values and Graylog 3.2 Data Table
> * Stacking fields on Legacy Terms (`Quick Values`) means intersecting `stacked fields` by `field` like an INTERSECTION
> * Adding fields on row groups of Terms (`Data Table`) means combining `fields` by order, like a UNION

Most common field terms of a query using a time range.
```
String from = "2020-07-30T00:00:00Z";
String to = "2020-07-31T00:00:00Z";

Timerange timerange = Timerange.builder()
    .type(TimeRangeType.absolute)
    .from(from)
    .to(to)
    .build();

List<Series> seriesList = List.of(
    Series.builder().type(SeriesType.count).build()
);

List<SearchTypePivot> rowGroups = List.of(
    SearchTypePivot.builder().type(SearchTypePivotType.values).field("field_name_1").limit(10).build(),
    SearchTypePivot.builder().type(SearchTypePivotType.values).field("field_name_2").build()
);

List<SearchTypePivot> columnGroups = List.of(
    SearchTypePivot.builder().type(SearchTypePivotType.values).field("field_name_3").limit(5).build()
);

SortConfig sort = SortConfig.builder()
    .type(SortConfigType.series)
    .field("count()")
    .direction(SortConfigDirection.Descending)
    .build();

graylogSearch.getTerms(
    List.of("graylog_stream_id"),
    timerange,
    "source:example.org",
    seriesList,
    rowGroups,
    columnGroups,
    sort
);
```

#### 1.5. Raw
Search with a search spec builder, returns raw response message from Graylog.
```
SearchSpec searchSpec = SearchSpec.builder()
    .query(
        Query.builder()
            .filter(
                Filter.builder()
                    .filters(
                        List.of(
                            SearchFilter.builder()
                                .id("graylog_stream_id")
                                .build()
                        )
                    )
                    .build()
            )
            .query(
                SearchQuery.builder()
                    .queryString("source:example.org")
                    .build()
            )
            .timerange(
                Timerange.builder()
                    .type(TimeRangeType.relative)
                    .range(300)
                    .build()
            )
            .searchType(
                SearchType.builder()
                    .name("chart")
                    .series(
                        List.of(
                            Series.builder()
                                .type(SeriesType.count)
                                .build()
                        )
                    )
                    .rollup(true)
                    .rowGroups(
                        List.of(
                            SearchTypePivot.builder()
                                .type(SearchTypePivotType.values)
                                .field("source")
                                .limit(5)
                                .build()
                        )
                    )
                    .columnGroups(List.of())
                    .sort(List.of())
                    .type(SearchTypeType.pivot)
                    .build()
            )
            .build()
    )
    .build();

String result = graylogSearch.raw(searchSpec);
```

### Graylog 3.2 Search Spec Builder

### 1. Outline of Search Spec
> With a search spec builder, it will generate required IDs automatically if not specified
* Each search has a search ID, which is made of [Object ID](https://mongodb.github.io/node-mongodb-native/api-bson-generated/objectid.html).
* Each query has a query ID, which is made of [UUID](https://docs.mongodb.com/manual/reference/method/UUID/).
* Each search type has a search type ID, which is made of [UUID](https://docs.mongodb.com/manual/reference/method/UUID/).
```
SearchSpec.builder() /* search ID */
    .query(
        Query.builder() /* query ID */
            .filter(... your filter ...)
            .query(... your search query ...)
            .timerange(... your timerange ...)
            .searchType( /* search type ID */
                ... your search type ...
            )
            .build()
    )
    .parameter(... your parameter if needed ...)
    .build();
```

### 2. Search Specifications

#### 2.1. Search Spec
> Data implementation of Graylog search UI as below

![Graylog Search UI](https://github.com/debugrammer/graylog-sdk-spring-boot/blob/2.x/images/graylog_search_ui.jpg)

Search spec contains search ID, queries, and parameters.

Generate search spec with specific search ID:
```
SearchSpec.builder()
    .id("your object id")
    ...
    .build();
```

Generate search spec with new search ID:
```
SearchSpec.builder()
    ...
    .build();
```

#### 2.2. Query
Query contains query ID, filter, search query, time range, and search types.

Generate query with specific query ID:
```
Query.builder()
    .id("your uuid")
    ...
    .build();
```

Generate query with new query ID:
```
Query.builder()
    ...
    .build();
```

#### 2.3. Filter
Graylog search filter. Equivalent to `stream select` on [Graylog search UI](#21-search-spec).
```
Filter.builder()
    .filters(
        List.of(
            SearchFilter.builder()
                .id("your graylog stream id")
                .build()
        )
    )
    .build();
```

#### 2.4. Timerange
Time range of search. Equivalent to `time range select` on [Graylog search UI](#21-search-spec).

Relative time range:
```
Timerange.builder()
    .type(TimeRangeType.relative)
    .range(300)
    .build();
```

Absolute time range:
> Requires [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) format
```
Timerange timerange = Timerange.builder()
    .type(TimeRangeType.absolute)
    .from("2020-07-30T00:00:00Z")
    .to("2020-07-31T00:00:00Z")
    .build();
```

Keyword time range:
```
Timerange timerange = Timerange.builder()
    .type(TimeRangeType.keyword)
    .keyword("Last five minutes")
    .build();
```

#### 2.5. Search Query
Graylog search query. Equivalent to `search query text field` on [Graylog search UI](#21-search-spec).
```
SearchQuery.builder()
    .queryString("your graylog search query")
    .build();
```

#### 2.6. Search Type
> Data implementation of Graylog widget aggregation UI as below

![Graylog Widget Aggregation UI](https://github.com/debugrammer/graylog-sdk-spring-boot/blob/2.x/images/graylog_widget_aggregation_ui.jpg)

Generate query with specific search type ID:
```
SearchType.builder()
    .id("your uuid")
    ...
    .build();
```

Generate query with new search type ID:
```
SearchType.builder()
    ...
    .build();
```

Full sample:
```
SearchType.builder()
    .name("chart") /* name of search type */
    .series( /* metrics */
        List.of(
            Series.builder()
                .type(SeriesType.count)
                .build()
        )
    )
    .rollup(true)
    .rowGroups( /* rows */
        List.of(
            SearchTypePivot.builder()
                .type(SearchTypePivotType.values)
                .field("source")
                .limit(15)
                .build()
        )
    )
    .columnGroups(List.of()) /* columns */
    .sort(List.of()) /* sorting */
    .type(SearchTypeType.pivot) /* pivot or messages */
    .build()
```

#### 2.6.1. Series
Equivalent to `METRICS` on [Graylog widget aggregation UI](#26-search-type).

count():
```
Series.builder()
    .type(SeriesType.count)
    .build();
```

avg(field_name):
```
Series.builder()
    .type(SeriesType.avg)
    .field("process_time")
    .build();
```

percentile(field_name, 95):
```
Series.builder()
    .type(SeriesType.percentile)
    .percentile(95.0f)
    .field("process_time")
    .build();
```

#### 2.6.2. Row Groups
Equivalent to `ROWS` on [Graylog widget aggregation UI](#26-search-type).
```
SearchTypePivot.builder()
    .type(SearchTypePivotType.values) /* values or time */
    .field("source")
    .limit(15)
    .build()
```

#### 2.6.3. Column Groups
Equivalent to `COLUMNS` on [Graylog widget aggregation UI](#26-search-type).
```
SearchTypePivot.builder()
    .type(SearchTypePivotType.values) /* values or time */
    .field("source")
    .limit(15)
    .build()
```

#### 2.6.4. Sort
Equivalent to `SORTING` and `DIRECTION` on [Graylog widget aggregation UI](#26-search-type).

message sort:
```
SortConfig.builder()
    .field("timestamp")
    .order(SortConfigOrder.DESC)
    .build();
```

pivot sort:
```
SortConfig.builder()
    .type(SortConfigType.series)
    .field("count()")
    .order(SortConfigDirection.Descending)
    .build();
```

## Legacy Graylog Search 
> Legacy search APIs will be no longer available from [Graylog 4.0](https://docs.graylog.org/en/3.3/pages/upgrade/graylog-3.3.html)

### Usage
Configure environment variables for Graylog SDK with `application.properties`:
```
# Graylog API Settings
graylog.sdk.api.scheme=http
graylog.sdk.api.host=localhost
graylog.sdk.api.port=9000
graylog.sdk.api.credentials=base64({graylog_access_token}:token)
graylog.sdk.legacy.timezone=US/Eastern
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
    legacy:
      timezone: US/Eastern
```

Then inject `LegacyGraylogSearch` bean in your project:
```
private final LegacyGraylogSearch legacyGraylogSearch;

public YourClassName(LegacyGraylogSearch legacyGraylogSearch) {
    this.legacyGraylogSearch = legacyGraylogSearch;
}
```

### Request Graylog REST APIs
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
List<YourMessageObject> messages = (List<YourMessageObject>) legacyGraylogSearch.getMessages(
    "graylog_stream_id",
    from,
    to,
    "request_id:AQZ4mfVGVqWKD38XZU7aVG",
    YourMessageObject.class
);
```

If you need messages with paging, try like below.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
int pageSize = 20;
int pageNo = 1;

@SuppressWarnings("unchecked")
Page<YourMessageObject> pagedMessages = (Page<YourMessageObject>) legacyGraylogSearch.getMessages(
    "graylog_stream_id",
    from,
    to,
    "request_id:AQZ4mfVGVqWKD38XZU7aVG",
    pageSize,
    pageNo,
    YourMessageObject.class
);
```

#### 1.2. Statistics
Field statistics for a query using an absolute time range.
```
LocalDateTime from = LocalDateTime.parse("2019-11-04 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
LocalDateTime to = LocalDateTime.parse("2019-11-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

Statistics statistics = legacyGraylogSearch.getStatistics(
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

Histogram histogram = legacyGraylogSearch.getHistogram(
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

FieldHistogram fieldHistogram = legacyGraylogSearch.getFieldHistogram(
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

Terms terms = legacyGraylogSearch.getTerms(
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
    * Check out [Graylog Query Builder](https://github.com/debugrammer/graylog-query-builder) if you are looking for query builder for [Graylog Search Query](https://docs.graylog.org/en/latest/pages/searching/query_language.html).

## Migration Guide From 1.2.x
You'll find a guide to upgrade from 1.2.x to 2.x [here](https://github.com/debugrammer/graylog-sdk-spring-boot/releases/tag/2.0.0).
