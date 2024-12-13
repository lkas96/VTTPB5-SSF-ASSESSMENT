FROM eclipse-temurin:23-jdk AS COMPILER

# labeling the dockerfile
# LABEL MAINTAINER="lawson"
# LABEL description="SSF DAY 17 - Carpark Project"
# LABEL name="VTTP-SSF-DAY17-LECTURE"

ARG COMPILE_DIR=/compiled

# directory where your source code will reside
# directory where you copy your project to (in the next step)
WORKDIR ${COMPILE_DIR}

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

COPY --from=compiler /compiled/target/vttp.batch5.ssf-0.0.1-SNAPSHOT.jar SSF-Assessment.jar

ENV SERVER_PORT=3000

EXPOSE ${SERVER_PORT}

ENTRYPOINT SERVER_PORT=${SERVER_PORT} java -jar VTTP_DAY17.jar