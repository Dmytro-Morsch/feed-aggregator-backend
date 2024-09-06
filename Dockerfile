FROM docker.io/amazoncorretto:17-alpine-jdk
COPY target/feed-aggregator.jar feed-aggregator.jar

ENTRYPOINT ["java","-jar","/feed-aggregator.jar"]