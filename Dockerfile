
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY mvnw pom.xml ./
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean install -DskipTests


FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/habitsTracker-1.0-SNAPSHOT-jar-with-dependencies.jar /app/habitsTracker-1.0-SNAPSHOT-jar-with-dependencies.jar
RUN mkdir -p src/main/resources
RUN cd src/main/resources
RUN touch config.properties
RUN echo "username=newAdmin" >>  src/main/resources/config.properties &&  \
    echo "password=5678" >>  src/main/resources/config.properties && \
    echo "url=jdbc:postgresql://tracker_app_database:5432/tracker_app_database" >>  src/main/resources/config.properties && \
    echo "changeLogFile=./db-changelog/main-changelog.xml" >>  src/main/resources/config.properties \

ENTRYPOINT ["java", "-jar", "/app/habitsTracker-1.0-SNAPSHOT-jar-with-dependencies.jar"]
