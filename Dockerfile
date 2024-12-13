FROM eclipse-temurin:23-jdk AS builder

ARG APP_DIR=/app

WORKDIR ${APP_DIR}

# copy the required files and/or directories into the image 
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY src src
COPY .mvn .mvn

# package the application using the RUN directive
# this will download the dependencies definedin pom.xml
# compile and package to jar
# RUN chmod a+x ./mvnw
RUN chmod a+x ./mvnw
RUN ./mvnw clean package -Dmaven.test.skip=true









# stage 2
FROM eclipse-temurin:23-jdk

ARG DEPLOY_DIR=/app

WORKDIR ${DEPLOY_DIR}

COPY --from=builder \ /app/target/noticeboard-0.0.1-SNAPSHOT.jar app.jar

ENV SERVER_PORT=3000

EXPOSE ${SERVER_PORT}

ENTRYPOINT java -jar app.jar