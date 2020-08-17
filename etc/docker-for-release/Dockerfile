# building skyline app stage
FROM openjdk:8-jdk-alpine as build

WORKDIR /app

RUN mkdir target
RUN mkdir -p target/dependency
COPY skyline.jar target

RUN cd target/dependency; jar -xf ../*.jar
COPY skyline-config/ /app/target/dependency/BOOT-INF/classes

# running skyline app stage
FROM openjdk:8-jre-alpine as production
ENV TZ=America/Sao_Paulo
ARG DEPENDENCY=/app/target/dependency

# copying build artifacts to run skyline app
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# running skyline app
ENTRYPOINT ["java", "-cp", "app:app/lib/*","com.papp.skyline.SkylineApplication"]
