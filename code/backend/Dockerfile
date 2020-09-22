FROM gradle:6.3.0-jdk8 as build
SHELL ["/bin/bash", "-c"]

WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN gradle clean build -x test
RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

## 2nd phase
FROM openjdk:8-jdk-alpine

ARG DEPENDENCY=/app/build/dependency
ARG MAIN_CLASS=ch.heiafr.pigroup6.passioncuisine.PassionCuisineApplication

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8080

CMD ["java", "-cp", "app:app/lib/*", "ch.heiafr.pigroup6.passioncuisine.PassionCuisineApplication"]
