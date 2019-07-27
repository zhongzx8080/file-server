FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_PATH ./target/file-server-java.jar
COPY ${JAR_PATH} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
