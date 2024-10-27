FROM openjdk:17-oracle
LABEL maintainer="keivan.shir.74@gmail.com"
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]