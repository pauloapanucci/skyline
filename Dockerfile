# building skyline app stage
FROM openjdk:8-jdk-alpine as build

ENV TZ=America/Sao_Paulo
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# downloading dependencys if need... may take a while at first build
RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# running skyline app stage
FROM openjdk:8-jre-alpine as production
ENV TZ=America/Sao_Paulo
ARG DEPENDENCY=/app/target/dependency

# copying build artifacts to run skyline app
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# running skyline app
ENTRYPOINT ["java", "-Dskyline.logFolder=logs", "-cp", "app:app/lib/*", "com.papp.skyline.SkylineApplication"]
