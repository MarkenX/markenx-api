FROM eclipse-temurin:21-jre

LABEL org.opencontainers.image.authors="markenx.udla.com"
LABEL org.opencontainers.image.source="https://github.com/udla/markenx"
LABEL org.opencontainers.image.description="MarkenX Backend API"

WORKDIR /app

COPY target/*jar app.jar

RUN addgroup --system app && adduser --system app --ingroup app
USER app

EXPOSE 8080

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]
