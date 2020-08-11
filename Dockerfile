# pull official base image
FROM openjdk:8u212-jre-alpine3.9

# set maintainer
LABEL maintainer "vilvamani007@gmail.com"

EXPOSE 8080

COPY target/spring-boot*SNAPSHOT.jar app.jar
COPY newrelic.jar newrelic.jar
COPY newrelic.yml newrelic.yml

ENV JAVA_OPTS="-javaagent:newrelic.jar -Xmx256m -Xms128m"
ENTRYPOINT java $JAVA_OPTS -jar /app.jar
