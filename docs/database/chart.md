# MarkenX API - Diagrama de Base de Datos

Este documento contiene el diagrama Entidad-Relación de la base de datos de MarkenX API.

## Diagrama ER

```mermaid
erDiagram
    %% ==========================================
    %% MÓDULO CLASSROOM
    %% ==========================================

    academic_terms {
        CHAR_36 id PK
        VARCHAR_20 lifecycle_status
        DATE start_date
        DATE end_date
        INT academic_year
        INT sequence
        VARCHAR_20 status
    }

    courses {
        CHAR_36 id PK
        VARCHAR_20 lifecycle_status
        VARCHAR_255 name
        INT code UK
        CHAR_36 academic_term_id FK
    }

    users {
        CHAR_36 id PK
        VARCHAR_20 lifecycle_status
        VARCHAR_255 email
    }

    students {
        CHAR_36 id PK
        VARCHAR_20 lifecycle_status
        VARCHAR_255 status
        VARCHAR_255 first_name
        VARCHAR_255 last_name
        INT code UK
        CHAR_36 course_id FK
        CHAR_36 user_id FK
    }

    student_summary_read_model {
        VARCHAR_36 student_id PK
        VARCHAR_150 email
        VARCHAR_200 full_name
    }

    tasks {
        CHAR_36 id PK
        VARCHAR_20 lifecycle_status
        VARCHAR_30 status
        INT code UK
        VARCHAR_255 title
        TEXT summary
        DATETIME deadline
        CHAR_36 course_id FK
        DECIMAL min_score_to_pass
        INT max_attempts
        INT current_attempt
    }

    %% ==========================================
    %% MÓDULO GAME - ENTIDADES BASE
    %% ==========================================

    dimensions {
        CHAR_36 id PK
        VARCHAR_255 name UK
        VARCHAR_255 display_name
        TEXT description
        DECIMAL consumer_expectation
        DECIMAL product_initial_offer
    }

    consumers {
        CHAR_36 id PK
        VARCHAR_255 name
        INT age
        DECIMAL budget
        DECIMAL target_acceptance_score
    }

    actions {
        CHAR_36 id PK
        VARCHAR_255 name UK
        TEXT description
        DECIMAL cost
        VARCHAR_20 category
        BOOLEAN is_initially_locked
        CHAR_36 prerequisite_action_id FK
    }

    action_effects {
        CHAR_36 id PK
        CHAR_36 action_id FK
        CHAR_36 dimension_id FK
        DECIMAL delta
    }

    game_events {
        CHAR_36 id PK
        VARCHAR_255 title UK
        TEXT description
    }

    event_effects {
        CHAR_36 id PK
        CHAR_36 event_id FK
        CHAR_36 dimension_id FK
        DECIMAL weight_multiplier
    }

    %% ==========================================
    %% MÓDULO GAME - ESCENARIOS
    %% ==========================================

    scenarios {
        CHAR_36 id PK
        VARCHAR_255 title UK
        TEXT description
        CHAR_36 consumer_id FK
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    scenario_dimensions {
        CHAR_36 scenario_id PK_FK
        CHAR_36 dimension_id PK_FK
    }

    scenario_actions {
        CHAR_36 scenario_id PK_FK
        CHAR_36 action_id PK_FK
    }

    scenario_events {
        CHAR_36 scenario_id PK_FK
        CHAR_36 event_id PK_FK
    }

    %% ==========================================
    %% RELACIONES - MÓDULO CLASSROOM
    %% ==========================================

    academic_terms ||--o{ courses : "tiene"
    courses ||--o{ students : "tiene"
    courses ||--o{ tasks : "tiene"
    users ||--o| students : "es asignado a"

    %% ==========================================
    %% RELACIONES - MÓDULO GAME
    %% ==========================================

    actions ||--o| actions : "prerequisito"
    actions ||--o{ action_effects : "tiene"
    dimensions ||--o{ action_effects : "afecta"

    game_events ||--o{ event_effects : "tiene"
    dimensions ||--o{ event_effects : "afecta"

    consumers ||--o{ scenarios : "usado en"

    scenarios ||--o{ scenario_dimensions : "incluye"
    dimensions ||--o{ scenario_dimensions : "incluido en"

    scenarios ||--o{ scenario_actions : "incluye"
    actions ||--o{ scenario_actions : "incluido en"

    scenarios ||--o{ scenario_events : "incluye"
    game_events ||--o{ scenario_events : "incluido en"
```

## Descripción de Módulos

### Módulo Classroom

El módulo Classroom gestiona la estructura académica:

- **academic_terms**: Periodos académicos con fechas y estados (UPCOMING, ACTIVE, ARCHIVED)
- **courses**: Cursos asociados a periodos académicos
- **users**: Usuarios del sistema (sincronizados con Keycloak)
- **students**: Estudiantes matriculados en cursos
- **student_summary_read_model**: Modelo de lectura CQRS para consultas optimizadas
- **tasks**: Tareas/asignaciones de los cursos con gestión de intentos

### Módulo Game

El módulo Game configura la simulación de videojuego:

- **dimensions**: Atributos medibles del juego (ej: calidad, precio, satisfacción)
- **consumers**: Perfiles de consumidores con expectativas y presupuesto
- **actions**: Acciones disponibles para el jugador con costos y prerrequisitos
- **action_effects**: Efectos de las acciones sobre las dimensiones
- **game_events**: Eventos aleatorios que ocurren durante el juego
- **event_effects**: Efectos de los eventos sobre las dimensiones
- **scenarios**: Configuraciones completas de juego que agrupan todos los elementos

## Patrones de Diseño

### CQRS (Command Query Responsibility Segregation)

- **Comandos (Escritura)**: Tablas principales con estructura normalizada
- **Consultas (Lectura)**: `student_summary_read_model` para consultas optimizadas

### Identificadores

- Todos los IDs son UUID (CHAR 36) para garantizar unicidad global
- Códigos auto-incrementales para referencia humana (courses.code, students.code, tasks.code)

### Estados y Ciclos de Vida

- `lifecycle_status`: Estado técnico del registro (soft delete pattern)
- `status`: Estado de negocio específico de cada entidad

### Integridad Referencial

- **ON DELETE RESTRICT**: Previene eliminación de registros referenciados
- **ON DELETE CASCADE**: Elimina registros hijos cuando se elimina el padre
- **ON DELETE SET NULL**: Permite eliminar referencias opcionales
