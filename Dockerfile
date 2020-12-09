FROM maven:3.6.3-jdk-11-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:11-slim
LABEL maintainer="lottier.jm@protonmail.com"
EXPOSE 8181 8000
COPY --from=build /workspace/target/*.jar eleonore.jar
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar eleonore.jar"]
