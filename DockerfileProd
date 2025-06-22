FROM openjdk:21-slim
ARG JAR_FILE=/danuri-rest/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]