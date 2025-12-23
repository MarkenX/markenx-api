FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

RUN addgroup --system app && adduser --system app --ingroup app
USER app

EXPOSE 8080
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]
