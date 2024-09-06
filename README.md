# Feed aggregator

Web-based RSS (Really Simple Syndication) feed aggregator. It allows users to subscribe to updates
from websites, blogs, and news sources by adding their
RSS feeds. This way, users can read all their favorite content in one centralized location without visiting each
website individually.

Project setup
-------------
Install JDK 17, Maven, and Postgres.

## Installation

Create database and app user:

``` sql
create database feed_aggregator;
create user feed_aggregator with encrypted password 'feed_aggregator';
grant all privileges on database feed_aggregator to feed_aggregator;
```

Create database and app user for `testing`:

``` sql
create database feed_aggregator_test;
create user feed_aggregator_test with encrypted password 'feed_aggregator_test';
grant all privileges on database feed_aggregator_test to feed_aggregator_test;
```

Create file `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=feed_aggregator
spring.datasource.password=feed_aggregator
```

Build backend:

```shell
mvn clean verify
```

Start backend server:

```shell
mvn spring-boot:run
```

Create secret key:

```shell
openssl rand 32 > dbencrypt.key
kubectl create secret generic feed-aggregator-dbencrypt --from-file=dbencrypt.key
```

Frontend part: https://github.com/Dmytro-Morsch/feed-aggregator-frontend