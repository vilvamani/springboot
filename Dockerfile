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
#COPY ../target/spring-boot*SNAPSHOT.jar app.jar
#COPY newrelic.jar newrelic.jar
#COPY infra/newrelic.yml newrelic.yml

#ENV JAVA_OPTS="-javaagent:newrelic.jar -Xmx256m -Xms128m"
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -javaagent:newrelic.jar -Xmx512m -Xms256m"
#ENTRYPOINT java $JAVA_OPTS -jar /app.jar

ENTRYPOINT ["java", $JAVA_OPTS, "-jar", "/app.jar"]