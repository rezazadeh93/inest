# syntax=docker/dockerfile:1

FROM openjdk:16-alpine3.13 as base

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src

FROM base as test
CMD ["./mvnw", "test"]

FROM base as development
CMD ./mvnw spring-boot:run \
    -Dspring-boot.run.profiles=dev \
    -Dspring-boot.run.arguments=--spring.datasource.url=jdbc:postgresql://postgresql/inest \
    -Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:2002'

FROM base as build
RUN ./mvnw package

FROM openjdk:11-jre-slim as production
EXPOSE 2000

COPY --from=build /app/target/inest-*.jar /application.jar

RUN sh -c 'touch /application.jar'

CMD java -Djava.security.egd=file:/dev/./urandom \
    -jar -Dspring.datasource.url=jdbc:postgresql://postgresql/inest \
    -Dspring.profiles.active=prod /application.jar
