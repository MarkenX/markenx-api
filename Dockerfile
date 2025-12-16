FROM eclipse-temurin:21-jdk

LABEL org.opencontainers.image.authors="markenx.udla.com"
LABEL org.opencontainers.image.source="https://github.com/MarkenX/markenx-api"
LABEL org.opencontainers.image.description="Backend API service for the MarkenX educational management system."

WORKDIR /app

COPY target/*jar app.jar

RUN addgroup --system app && adduser --system app --ingroup app
USER app

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]
