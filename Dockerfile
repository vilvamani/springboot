# Start with a base image containing Java runtime (mine java 8)
FROM openjdk:8u212-jre-alpine3.9

# Add Maintainer Info
LABEL maintainer "vilvamani007@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file (when packaged)
ARG JAR_FILE=target/spring-boot*SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

ENTRYPOINT java -jar /app.jar