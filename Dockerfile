# pull official base image
FROM openjdk:8u212-jre-alpine3.9

# set maintainer
LABEL maintainer "vilvamani007@gmail.com"

EXPOSE 8080

COPY target/spring-boot*SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-javaagent:newrelic.jar"
ENTRYPOINT java $JAVA_OPTS -jar /app.jar
