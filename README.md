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
CREATE USER feed_aggregator WITH ENCRYPTED PASSWORD 'feed_aggregator';
CREATE SCHEMA feed_aggregator AUTHORIZATION feed_aggregator;
GRANT USAGE ON SCHEMA feed_aggregator TO feed_aggregator;
ALTER ROLE feed_aggregator SET search_path TO feed_aggregator;
```

Create database and app user for `testing`:

``` sql
CREATE USER feed_aggregator_test WITH ENCRYPTED PASSWORD 'feed_aggregator_test';
CREATE SCHEMA feed_aggregator_test AUTHORIZATION feed_aggregator_test;
GRANT USAGE ON SCHEMA feed_aggregator_test TO feed_aggregator_test;
ALTER ROLE feed_aggregator_test SET search_path TO feed_aggregator_test;
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