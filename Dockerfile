FROM maven:3-jdk-11 AS build

ADD . /build
WORKDIR /build
RUN ls -l
RUN mvn clean install

FROM openjdk:11-jdk

COPY --from=build /build/target/noc-0.0.1.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]