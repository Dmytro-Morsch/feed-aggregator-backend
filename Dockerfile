FROM docker.io/amazoncorretto:17-alpine-jdk
WORKDIR app
COPY target/feed-aggregator.jar feed-aggregator.jar

ENTRYPOINT ["java","-jar","/app/feed-aggregator.jar"]