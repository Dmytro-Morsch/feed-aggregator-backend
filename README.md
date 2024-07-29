# Feed aggregator

## Installation

Create database and application user

``` sql
create database feed_aggregator;
create user feed_aggregator with encrypted password 'feed_aggregator';
grant all privileges on database feed_aggregator to feed_aggregator;
```

Create database for `testing` and application user

``` sql
create database feed_aggregator_test;
create user feed_aggregator_test with encrypted password 'feed_aggregator_test';
grant all privileges on database feed_aggregator_test to feed_aggregator_test;
```

Create file `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=feed_aggregator
spring.datasource.password=feed_aggregator
```