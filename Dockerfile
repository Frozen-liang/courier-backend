# Version 0.0.1
FROM openjdk:8u212-jdk-stretch
MAINTAINER pader.zhang "pader.zhang@starlight-sms.com"
ARG RELEASE_VERSION=1.0.0-SNAPSHOT
RUN mkdir /root/sms-satp/
WORKDIR /root/sms-satp/
ADD ./target/sms-satp-${RELEASE_VERSION}.jar sms-satp.jar
ADD ./target/application.properties application.properties
EXPOSE 80
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar sms-satp.jar --spring.config.location=./application.properties"]
