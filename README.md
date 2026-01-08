# MarkenX API

API backend para el sistema MarkenX, una plataforma de simulación de videojuegos de estrategia de marketing para educación universitaria.

## Descripción

MarkenX es un sistema educativo que permite a estudiantes universitarios aprender estrategias de marketing a través de simulaciones interactivas. La API proporciona endpoints para:

- **Gestión académica**: Periodos académicos, cursos y estudiantes
- **Configuración de juego**: Escenarios, dimensiones, acciones y eventos
- **Registro de partidas**: Resultados de sesiones de juego con historial de turnos

## Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 21 | Lenguaje de programación |
| Spring Boot | 3.x | Framework principal |
| Spring Modulith | - | Modularización |
| MySQL | 8.0 | Base de datos |
| Flyway | - | Migraciones de BD |
| JDBC | - | Persistencia (Commands) |
| jOOQ | - | Persistencia (Queries) |
| Keycloak | 25.0.5 | Gestión de identidad |
| Maven | - | Gestión de dependencias |
| Docker | - | Contenedorización |

## Arquitectura

El proyecto sigue los principios de:

- **Domain-Driven Design (DDD)**: Modelado del dominio con agregados, entidades y value objects
- **Arquitectura Hexagonal**: Separación de puertos y adaptadores
- **CQRS**: Separación de comandos (JDBC) y consultas (jOOQ)
- **Saga Pattern**: Coordinación de transacciones distribuidas con Keycloak

## Prerequisitos

- Java 21 o superior
- Docker y Docker Compose
- Maven 3.8+

## Ejecución en Entorno DEV

### 1. Clonar el repositorio

```bash
git clone https://github.com/MarkenX/markenx.api.git
cd markenx.api
```

### 2. Configurar variables de entorno

Crear archivo `.env` en la raíz del proyecto:

```env
# Keycloak
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin
KEYCLOAK_REALM=markenx
KEYCLOAK_CLIENT=markenx-api
KEYCLOAK_DB_NAME=keycloak
KEYCLOAK_DB_USER=keycloak
KEYCLOAK_DB_PASSWORD=keycloak
KEYCLOAK_SCHEME=http
KEYCLOAK_HOST=localhost
KEYCLOAK_PORT=9090

# MySQL
DB_NAME=markenx
DB_PASSWORD=root
```

### 3. Iniciar servicios con Docker Compose

```bash
docker compose up -d
```

Esto iniciará:
- **Keycloak** en `http://localhost:9090`
- **PostgreSQL** (para Keycloak) en puerto `5432`
- **MySQL** en puerto `3306`

### 4. Ejecutar la aplicación

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

La API estará disponible en `http://localhost:8080`

### 5. Acceso a Swagger UI

```
http://localhost:8080/swagger-ui.html
```

## Comandos Disponibles

| Comando | Descripción |
|---------|-------------|
| `./mvnw clean compile` | Compilar el proyecto |
| `./mvnw test` | Ejecutar pruebas unitarias |
| `./mvnw verify` | Ejecutar todas las pruebas |
| `./mvnw package` | Generar JAR |
| `./mvnw spring-boot:run` | Ejecutar la aplicación |
| `./mvnw flyway:migrate` | Ejecutar migraciones de BD |
| `./mvnw flyway:info` | Ver estado de migraciones |

## Estructura del Proyecto

```
src/main/java/com/udla/markenx/api/
├── classroom/                    # Módulo de gestión académica
│   ├── academicterms/           # Periodos académicos
│   ├── assignments/             # Tareas (Tasks)
│   ├── courses/                 # Cursos
│   └── students/                # Estudiantes
├── game/                        # Módulo de videojuego
│   ├── actions/                 # Acciones de marketing
│   ├── attempts/                # Resultados de partidas
│   ├── consumers/               # Consumidores target
│   ├── dimensions/              # Dimensiones de evaluación
│   ├── events/                  # Eventos del juego
│   └── scenarios/               # Escenarios de simulación
└── shared/                      # Componentes compartidos
    ├── domain/                  # Clases base de dominio
    └── infrastructure/          # Infraestructura común
```

Cada módulo sigue la estructura hexagonal:

```
modulo/
├── application/
│   ├── commands/               # Comandos (write operations)
│   ├── dtos/                   # Data Transfer Objects
│   ├── ports/
│   │   └── incoming/           # Use cases (interfaces)
│   └── services/               # Handlers de comandos y queries
├── domain/
│   ├── exceptions/             # Excepciones de dominio
│   ├── models/
│   │   ├── aggregates/         # Agregados
│   │   ├── entities/           # Entidades
│   │   └── valueobjects/       # Value objects
│   └── ports/
│       └── outgoing/           # Repository interfaces
└── infrastructure/
    ├── persistence/
    │   ├── jdbc/               # Repositorios JDBC (commands)
    │   └── jooq/               # Repositorios jOOQ (queries)
    └── web/
        └── rest/               # Controllers REST
```

## CI/CD

### Integración Continua (CircleCI)

El pipeline de CI se ejecuta en este repositorio con CircleCI:

**Workflow `main` (rama main):**
```
build → test → sonar → package
```

**Workflow `release` (tags v*):**
```
build → test → package → docker_build_push → github_release → notify_slack
```

### Despliegue Continuo (ArgoCD)

El CD se gestiona desde el repositorio externo:
- **Repositorio**: https://github.com/MarkenX/markenx-config

ArgoCD monitorea los cambios en `markenx-config` y despliega automáticamente a Kubernetes.

## Documentación

| Documento | Descripción |
|-----------|-------------|
| [docs/api/endpoints.md](docs/api/endpoints.md) | Documentación completa de la API REST |
| [docs/database/tables.md](docs/database/tables.md) | Esquema de base de datos |
| [docs/database/chart.md](docs/database/chart.md) | Diagrama Entidad-Relación |
| [docs/videogame-config.md](docs/videogame-config.md) | Configuración del sistema de juego |

## Módulos de la API

### Classroom (Gestión Académica)

- **Academic Terms**: Gestión de periodos académicos (UPCOMING → ACTIVE → ARCHIVED)
- **Courses**: Cursos asociados a periodos académicos
- **Students**: Estudiantes con integración Keycloak (Saga Pattern)
- **Tasks**: Tareas/asignaciones con intentos limitados

### Game (Videojuego)

- **Scenarios**: Escenarios de simulación de marketing
- **Dimensions**: Dimensiones de evaluación (precio, calidad, marca, etc.)
- **Consumers**: Perfiles de consumidor target
- **Actions**: Acciones de marketing disponibles con efectos
- **Events**: Eventos aleatorios que afectan el juego
- **Attempts**: Registro de resultados de partidas

## Licencia

Este proyecto es de uso educativo para la Universidad de Las Américas (UDLA).

---

Desarrollado con Spring Boot y mucho ☕
