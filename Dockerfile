FROM openjdk:11-slim

LABEL maintainer="lottier.jm@protonmail.com"

EXPOSE 8181 8000

ENV APP_HOME /app
ENV JAVA_OPTS=""

RUN mkdir $APP_HOME &&\
    mkdir $APP_HOME/config &&\
    mkdir $APP_HOME/log

VOLUME $APP_HOME/log
VOLUME $APP_HOME/config

WORKDIR $APP_HOME
COPY /target/*.jar eleonore.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar eleonore.jar"]