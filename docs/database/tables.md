# MarkenX API - Documentación de Base de Datos

Esta documentación describe todas las tablas de la base de datos MySQL utilizada por MarkenX API.

---

## Tabla de Contenidos

1. [Módulo Classroom](#módulo-classroom)
   - [academic_terms](#academic_terms)
   - [courses](#courses)
   - [users](#users)
   - [students](#students)
   - [student_summary_read_model](#student_summary_read_model)
   - [tasks](#tasks)
2. [Módulo Game](#módulo-game)
   - [dimensions](#dimensions)
   - [consumers](#consumers)
   - [actions](#actions)
   - [action_effects](#action_effects)
   - [game_events](#game_events)
   - [event_effects](#event_effects)
   - [scenarios](#scenarios)
   - [scenario_dimensions](#scenario_dimensions)
   - [scenario_actions](#scenario_actions)
   - [scenario_events](#scenario_events)

---

## Módulo Classroom

### academic_terms

Almacena los periodos académicos del sistema.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| lifecycle_status | VARCHAR(20) | NO | Estado del ciclo de vida |
| start_date | DATE | NO | Fecha de inicio del periodo |
| end_date | DATE | NO | Fecha de fin del periodo |
| academic_year | INT | NO | Año académico |
| sequence | INT | NO | Secuencia dentro del año |
| status | VARCHAR(20) | NO | Estado: UPCOMING, ACTIVE, ARCHIVED |

**Constraints:**
- `pk_academic_terms` - Primary Key (id)
- `uk_academic_terms_year_sequence` - Unique (academic_year, sequence)

---

### courses

Almacena los cursos asociados a periodos académicos.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| lifecycle_status | VARCHAR(20) | NO | Estado del ciclo de vida: ACTIVE, ARCHIVED |
| name | VARCHAR(255) | NO | Nombre del curso |
| code | INT | NO | Código único auto-incremental |
| academic_term_id | CHAR(36) | NO | FK al periodo académico |

**Constraints:**
- `pk_courses` - Primary Key (id)
- `uk_courses_code` - Unique (code)
- `fk_courses_academic_term` - Foreign Key (academic_term_id) → academic_terms(id)

**Índices:**
- `idx_courses_academic_term` - (academic_term_id)

---

### users

Almacena los usuarios del sistema (sincronizados con Keycloak).

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| lifecycle_status | VARCHAR(20) | NO | Estado del ciclo de vida |
| email | VARCHAR(255) | NO | Correo electrónico |

**Constraints:**
- Primary Key (id)

---

### students

Almacena los estudiantes registrados en cursos.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| lifecycle_status | VARCHAR(20) | NO | Estado del ciclo de vida |
| status | VARCHAR(255) | NO | Estado: PENDING_IDENTITY, ACTIVE, DISABLED |
| first_name | VARCHAR(255) | NO | Nombre del estudiante |
| last_name | VARCHAR(255) | NO | Apellido del estudiante |
| code | INT | NO | Código único auto-incremental |
| course_id | CHAR(36) | NO | FK al curso |
| user_id | CHAR(36) | SI | FK al usuario (Keycloak) |

**Constraints:**
- Primary Key (id)
- Unique (code)
- `fk_students_course` - Foreign Key (course_id) → courses(id)
- `fk_students_user` - Foreign Key (user_id) → users(id)

**Índices:**
- `idx_students_course` - (course_id)
- `idx_students_user` - (user_id)

---

### student_summary_read_model

Modelo de lectura (CQRS) para consultas de estudiantes con información de usuario.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| student_id | VARCHAR(36) | NO | Identificador del estudiante - PK |
| email | VARCHAR(150) | NO | Correo electrónico |
| full_name | VARCHAR(200) | NO | Nombre completo |

**Índices:**
- `idx_student_email` - (email)

---

### tasks

Almacena las tareas (assignments) de los cursos.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| lifecycle_status | VARCHAR(20) | NO | Estado del ciclo de vida |
| status | VARCHAR(30) | NO | Estado: NOT_STARTED, IN_PROGRESS, FINISHED |
| code | INT | NO | Código único auto-incremental |
| title | VARCHAR(255) | NO | Título de la tarea |
| summary | TEXT | SI | Descripción de la tarea |
| deadline | DATETIME | NO | Fecha límite de entrega |
| course_id | CHAR(36) | NO | FK al curso |
| min_score_to_pass | DECIMAL(3,2) | NO | Puntaje mínimo para aprobar (0-1) |
| max_attempts | INT | NO | Número máximo de intentos (>0) |
| current_attempt | INT | NO | Intento actual (>=0) |

**Constraints:**
- `pk_tasks` - Primary Key (id)
- `uk_tasks_code` - Unique (code)
- `fk_tasks_course` - Foreign Key (course_id) → courses(id)
- CHECK (min_score_to_pass >= 0 AND min_score_to_pass <= 1)
- CHECK (max_attempts > 0)
- CHECK (current_attempt >= 0)

**Índices:**
- `idx_tasks_course` - (course_id)
- `idx_tasks_status` - (status)

---

## Módulo Game

### dimensions

Almacena las dimensiones del juego que representan atributos medibles.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| name | VARCHAR(255) | NO | Nombre único de la dimensión |
| display_name | VARCHAR(255) | NO | Nombre para mostrar |
| description | TEXT | NO | Descripción de la dimensión |
| consumer_expectation | DECIMAL(3,2) | NO | Expectativa del consumidor (0-1) |
| product_initial_offer | DECIMAL(3,2) | NO | Oferta inicial del producto (0-1) |

**Constraints:**
- `pk_dimensions` - Primary Key (id)
- `uk_dimensions_name` - Unique (name)
- CHECK (consumer_expectation >= 0 AND consumer_expectation <= 1)
- CHECK (product_initial_offer >= 0 AND product_initial_offer <= 1)

**Índices:**
- `idx_dimensions_consumer_expectation` - (consumer_expectation)
- `idx_dimensions_product_initial_offer` - (product_initial_offer)

---

### consumers

Almacena los perfiles de consumidores del juego.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| name | VARCHAR(255) | NO | Nombre del consumidor |
| age | INT | SI | Edad (0-150) |
| budget | DECIMAL(10,2) | SI | Presupuesto (>=0) |
| target_acceptance_score | DECIMAL(3,2) | NO | Puntaje de aceptación objetivo (0-1) |

**Constraints:**
- `pk_consumers` - Primary Key (id)
- CHECK (age >= 0 AND age <= 150)
- CHECK (budget >= 0)
- CHECK (target_acceptance_score >= 0 AND target_acceptance_score <= 1)

**Índices:**
- `idx_consumers_name` - (name)
- `idx_consumers_target_acceptance_score` - (target_acceptance_score)

---

### actions

Almacena las acciones disponibles en el juego.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| name | VARCHAR(255) | NO | Nombre único de la acción |
| description | TEXT | NO | Descripción de la acción |
| cost | DECIMAL(10,2) | NO | Costo de la acción (>=0) |
| category | VARCHAR(20) | NO | Categoría de la acción |
| is_initially_locked | BOOLEAN | NO | Si está bloqueada inicialmente (default FALSE) |
| prerequisite_action_id | CHAR(36) | SI | FK a acción prerequisito |

**Constraints:**
- `pk_actions` - Primary Key (id)
- `uk_actions_name` - Unique (name)
- `fk_actions_prerequisite` - Foreign Key (prerequisite_action_id) → actions(id) ON DELETE SET NULL
- CHECK (cost >= 0)

**Índices:**
- `idx_actions_category` - (category)
- `idx_actions_prerequisite` - (prerequisite_action_id)
- `idx_actions_cost` - (cost)

---

### action_effects

Almacena los efectos que las acciones tienen sobre las dimensiones.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| action_id | CHAR(36) | NO | FK a la acción |
| dimension_id | CHAR(36) | NO | FK a la dimensión |
| delta | DECIMAL(4,2) | NO | Cambio en la dimensión (-1 a 1) |

**Constraints:**
- `pk_action_effects` - Primary Key (id)
- `fk_action_effects_action` - Foreign Key (action_id) → actions(id) ON DELETE CASCADE
- `fk_action_effects_dimension` - Foreign Key (dimension_id) → dimensions(id) ON DELETE RESTRICT
- `uk_action_effects_unique` - Unique (action_id, dimension_id)
- CHECK (delta >= -1 AND delta <= 1)

**Índices:**
- `idx_action_effects_action` - (action_id)
- `idx_action_effects_dimension` - (dimension_id)

---

### game_events

Almacena los eventos del juego que pueden ocurrir durante la simulación.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| title | VARCHAR(255) | NO | Título único del evento |
| description | TEXT | NO | Descripción del evento |

**Constraints:**
- `pk_game_events` - Primary Key (id)
- `uk_game_events_title` - Unique (title)

**Índices:**
- `idx_game_events_title` - (title)

---

### event_effects

Almacena los efectos que los eventos tienen sobre las dimensiones.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| event_id | CHAR(36) | NO | FK al evento |
| dimension_id | CHAR(36) | NO | FK a la dimensión |
| weight_multiplier | DECIMAL(5,2) | NO | Multiplicador de peso (>=0) |

**Constraints:**
- `pk_event_effects` - Primary Key (id)
- `fk_event_effects_event` - Foreign Key (event_id) → game_events(id) ON DELETE CASCADE
- `fk_event_effects_dimension` - Foreign Key (dimension_id) → dimensions(id) ON DELETE RESTRICT
- `uk_event_effects_unique` - Unique (event_id, dimension_id)
- CHECK (weight_multiplier >= 0)

**Índices:**
- `idx_event_effects_event` - (event_id)
- `idx_event_effects_dimension` - (dimension_id)

---

### scenarios

Almacena los escenarios de juego que agrupan configuraciones completas.

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| id | CHAR(36) | NO | Identificador único (UUID) - PK |
| title | VARCHAR(255) | NO | Título único del escenario |
| description | TEXT | NO | Descripción del escenario |
| consumer_id | CHAR(36) | SI | FK al consumidor asociado |
| created_at | TIMESTAMP | NO | Fecha de creación (auto) |
| updated_at | TIMESTAMP | NO | Fecha de actualización (auto) |

**Constraints:**
- `pk_scenarios` - Primary Key (id)
- `uk_scenarios_title` - Unique (title)
- `fk_scenarios_consumer` - Foreign Key (consumer_id) → consumers(id) ON DELETE SET NULL

**Índices:**
- `idx_scenarios_title` - (title)
- `idx_scenarios_consumer` - (consumer_id)
- `idx_scenarios_created_at` - (created_at)

---

### scenario_dimensions

Tabla intermedia que relaciona escenarios con dimensiones (N:M).

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| scenario_id | CHAR(36) | NO | FK al escenario - PK |
| dimension_id | CHAR(36) | NO | FK a la dimensión - PK |

**Constraints:**
- Primary Key (scenario_id, dimension_id)
- `fk_scenario_dimensions_scenario` - Foreign Key (scenario_id) → scenarios(id) ON DELETE CASCADE
- `fk_scenario_dimensions_dimension` - Foreign Key (dimension_id) → dimensions(id) ON DELETE RESTRICT

**Índices:**
- `idx_scenario_dimensions_scenario` - (scenario_id)
- `idx_scenario_dimensions_dimension` - (dimension_id)

---

### scenario_actions

Tabla intermedia que relaciona escenarios con acciones (N:M).

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| scenario_id | CHAR(36) | NO | FK al escenario - PK |
| action_id | CHAR(36) | NO | FK a la acción - PK |

**Constraints:**
- Primary Key (scenario_id, action_id)
- `fk_scenario_actions_scenario` - Foreign Key (scenario_id) → scenarios(id) ON DELETE CASCADE
- `fk_scenario_actions_action` - Foreign Key (action_id) → actions(id) ON DELETE RESTRICT

**Índices:**
- `idx_scenario_actions_scenario` - (scenario_id)
- `idx_scenario_actions_action` - (action_id)

---

### scenario_events

Tabla intermedia que relaciona escenarios con eventos (N:M).

| Columna | Tipo | Nullable | Descripción |
|---------|------|----------|-------------|
| scenario_id | CHAR(36) | NO | FK al escenario - PK |
| event_id | CHAR(36) | NO | FK al evento - PK |

**Constraints:**
- Primary Key (scenario_id, event_id)
- `fk_scenario_events_scenario` - Foreign Key (scenario_id) → scenarios(id) ON DELETE CASCADE
- `fk_scenario_events_event` - Foreign Key (event_id) → game_events(id) ON DELETE RESTRICT

**Índices:**
- `idx_scenario_events_scenario` - (scenario_id)
- `idx_scenario_events_event` - (event_id)

---

## Resumen de Migraciones Flyway

| Versión | Descripción |
|---------|-------------|
| V1 | create_academic_terms |
| V2 | create_courses |
| V3 | create_users |
| V4 | create_students |
| V5 | create_student_user_read_model |
| V6 | create_tasks |
| V7 | create_dimensions |
| V8 | create_consumers |
| V9 | create_actions |
| V10 | create_action_effects |
| V11 | create_game_events |
| V12 | create_event_effects |
| V13 | create_scenarios |
| V14 | create_scenario_dimensions |
| V15 | create_scenario_actions |
| V16 | create_scenario_events |
