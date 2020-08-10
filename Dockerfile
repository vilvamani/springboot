# pull official base image
FROM openjdk:8u212-jre-alpine3.9

# set maintainer
LABEL maintainer "vilvamani007@gmail.com"

EXPOSE 8080

COPY target/springboot*SNAPSHOT.jar app.jar

#ENV JAVA_OPTS="-javaagent:/appd-java-agent/javaagent.jar"
#ENTRYPOINT java $JAVA_OPTS -jar /app.jar

ENTRYPOINT java -jar /app.jar
