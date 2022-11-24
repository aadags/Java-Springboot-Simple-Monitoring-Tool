FROM maven:3-jdk-11

ADD . /build
WORKDIR /build
RUN ls -l
RUN mvn clean install
FROM openjdk:11-jdk

ARG JAR_FILE=target/noc-0.0.1.jar

WORKDIR /opt/app

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]