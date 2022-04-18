FROM openjdk:11-jre-slim
ENV APP_NAME kalah

COPY build/libs/${APP_NAME}*.jar /${APP_NAME}.jar

ENTRYPOINT ["java","-jar","/kalah.jar"]