FROM eclipse-temurin:21-jdk AS build

WORKDIR /build

# Copiar solo lo necesario primero para aprovechar cache
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline

# Copiar el código fuente
COPY src src

# Construir el JAR
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre

LABEL org.opencontainers.image.authors="markenx.udla.com"
LABEL org.opencontainers.image.source="https://github.com/MarkenX/markenx-api"
LABEL org.opencontainers.image.description="Backend API service for the MarkenX educational management system."

WORKDIR /app

# Copiar solo el JAR final
COPY --from=build /build/target/*.jar app.jar

# Seguridad: usuario no root
RUN addgroup --system app && adduser --system app --ingroup app
USER app

EXPOSE 8082

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]
