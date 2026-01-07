# MarkenX API - Documentación de Endpoints

Esta documentación describe todos los endpoints expuestos por la API de MarkenX, organizados por módulo.

---

## Tabla de Contenidos

1. [Módulo Academic Terms](#módulo-academic-terms)
2. [Módulo Courses](#módulo-courses)
3. [Módulo Students](#módulo-students)
4. [Módulo Assignments (Tasks)](#módulo-assignments-tasks)
5. [Módulo Scenarios](#módulo-scenarios)

---

## Módulo Academic Terms

Base path: `/api/academic-terms`

### Crear Periodo Académico

```
POST /api/academic-terms
```

**Descripción:** Crea un nuevo periodo académico con estado inicial UPCOMING.

**Request Body:**

```json
{
  "name": "string",
  "startDate": "YYYY-MM-DD",
  "endDate": "YYYY-MM-DD"
}
```

**Response:** `201 Created`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "startDate": "YYYY-MM-DD",
  "endDate": "YYYY-MM-DD",
  "status": "UPCOMING"
}
```

**Errores posibles:**

- `400 Bad Request` - Datos de entrada inválidos

---

### Obtener Periodo Académico por ID

```
GET /api/academic-terms/{id}
```

**Descripción:** Obtiene los detalles de un periodo académico específico.

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|--------------------------------|
| id | string | UUID del periodo académico |

**Response:** `200 OK`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "startDate": "YYYY-MM-DD",
  "endDate": "YYYY-MM-DD",
  "status": "UPCOMING | ACTIVE | ARCHIVED"
}
```

**Errores posibles:**

- `404 Not Found` - `AcademicTermNotFoundException`: El periodo académico con ID especificado no existe

---

### Cambiar Estado del Periodo Académico

```
PATCH /api/academic-terms/{id}/status
```

**Descripción:** Cambia el estado de un periodo académico. Las transiciones válidas son:

- UPCOMING → ACTIVE
- ACTIVE → ARCHIVED

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|--------------------------------|
| id | string | UUID del periodo académico |

**Request Body:**

```json
{
  "status": "UPCOMING | ACTIVE | ARCHIVED"
}
```

**Response:** `200 OK`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "startDate": "YYYY-MM-DD",
  "endDate": "YYYY-MM-DD",
  "status": "UPCOMING | ACTIVE | ARCHIVED"
}
```

**Errores posibles:**

- `404 Not Found` - `AcademicTermNotFoundException`: El periodo académico no existe
- `400 Bad Request` - `InvalidStatusTransitionException`: Transición de estado no permitida

---

### Actualizar Periodo Académico

```
PUT /api/academic-terms/{id}
```

**Descripción:** Actualiza los datos de un periodo académico existente. Solo se pueden modificar periodos con estado
UPCOMING.

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|--------------------------------|
| id | string | UUID del periodo académico |

**Request Body:**

```json
{
  "name": "string",
  "startDate": "YYYY-MM-DD",
  "endDate": "YYYY-MM-DD"
}
```

**Response:** `200 OK`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "startDate": "YYYY-MM-DD",
  "endDate": "YYYY-MM-DD",
  "status": "UPCOMING"
}
```

**Errores posibles:**

- `404 Not Found` - `AcademicTermNotFoundException`: El periodo académico no existe
- `400 Bad Request` - `AcademicTermNotUpcomingException`: Solo se pueden modificar periodos con estado UPCOMING

---

### Listar Todos los Periodos Académicos

```
GET /api/academic-terms
```

**Descripción:** Obtiene la lista de todos los periodos académicos registrados.

**Response:** `200 OK`

```json
[
  {
    "id": "string (UUID)",
    "name": "string",
    "startDate": "YYYY-MM-DD",
    "endDate": "YYYY-MM-DD",
    "status": "UPCOMING | ACTIVE | ARCHIVED"
  }
]
```

---

## Módulo Courses

Base path: `/api/courses`

### Crear Curso

```
POST /api/courses
```

**Descripción:** Crea un nuevo curso asociado a un periodo académico. El periodo académico debe estar en estado
UPCOMING.

**Request Body:**

```json
{
  "name": "string",
  "academicTermId": "string (UUID)"
}
```

**Response:** `201 Created`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "lifecycleStatus": "ACTIVE",
  "academicTermId": "string (UUID)"
}
```

**Errores posibles:**

- `404 Not Found` - `AcademicTermNotFoundException`: El periodo académico no existe
- `400 Bad Request` - `AcademicTermNotUpcomingException`: El periodo académico debe estar en estado UPCOMING para crear
  cursos

---

### Obtener Curso por ID

```
GET /api/courses/{id}
```

**Descripción:** Obtiene los detalles de un curso específico.

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|--------------------|
| id | string | UUID del curso |

**Response:** `200 OK`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "lifecycleStatus": "ACTIVE | ARCHIVED",
  "academicTermId": "string (UUID)"
}
```

**Errores posibles:**

- `404 Not Found` - `CourseNotFoundException`: El curso con ID especificado no existe

---

### Cambiar Estado del Curso

```
PATCH /api/courses/{id}/status
```

**Descripción:** Cambia el estado del ciclo de vida de un curso.

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|--------------------|
| id | string | UUID del curso |

**Request Body:**

```json
{
  "status": "ACTIVE | ARCHIVED"
}
```

**Response:** `200 OK`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "lifecycleStatus": "ACTIVE | ARCHIVED",
  "academicTermId": "string (UUID)"
}
```

**Errores posibles:**

- `404 Not Found` - `CourseNotFoundException`: El curso no existe
- `400 Bad Request` - `InvalidCourseStatusTransitionException`: Transición de estado no permitida

---

### Cambiar Periodo Académico del Curso

```
PUT /api/courses/{id}/change-academic-term
```

**Descripción:** Cambia el periodo académico asociado a un curso. El nuevo periodo debe estar en estado UPCOMING.

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|--------------------|
| id | string | UUID del curso |

**Request Body:**

```json
{
  "academicTermId": "string (UUID)"
}
```

**Response:** `200 OK`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "lifecycleStatus": "ACTIVE | ARCHIVED",
  "academicTermId": "string (UUID)"
}
```

**Errores posibles:**

- `404 Not Found` - `CourseNotFoundException`: El curso no existe
- `404 Not Found` - `AcademicTermNotFoundException`: El periodo académico no existe
- `400 Bad Request` - `AcademicTermNotUpcomingException`: El nuevo periodo académico debe estar en estado UPCOMING

---

### Actualizar Curso

```
PUT /api/courses/{id}
```

**Descripción:** Actualiza los datos de un curso existente.

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|--------------------|
| id | string | UUID del curso |

**Request Body:**

```json
{
  "name": "string"
}
```

**Response:** `200 OK`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "lifecycleStatus": "ACTIVE | ARCHIVED",
  "academicTermId": "string (UUID)"
}
```

**Errores posibles:**

- `404 Not Found` - `CourseNotFoundException`: El curso no existe

---

### Listar Todos los Cursos

```
GET /api/courses
```

**Descripción:** Obtiene la lista de todos los cursos registrados.

**Response:** `200 OK`

```json
[
  {
    "id": "string (UUID)",
    "name": "string",
    "lifecycleStatus": "ACTIVE | ARCHIVED",
    "academicTermId": "string (UUID)"
  }
]
```

---

## Módulo Students

Base path: `/api/students`

### Registrar Estudiante

```
POST /api/students
```

**Descripción:** Registra un nuevo estudiante en un curso. El curso debe pertenecer a un periodo académico con estado
UPCOMING. Adicionalmente, se crea una identidad en Keycloak para el estudiante mediante el patrón Saga.

**Request Body:**

```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string (email)",
  "courseId": "string (UUID)"
}
```

**Response:** `201 Created`

```json
{
  "id": "string (UUID)",
  "firstName": "string",
  "lastName": "string",
  "courseId": "string (UUID)",
  "status": "PENDING_IDENTITY",
  "userId": null
}
```

**Nota:** El campo `userId` se actualiza automáticamente cuando se completa la creación de identidad en Keycloak (
proceso asíncrono via Saga).

**Estados del estudiante:**

- `PENDING_IDENTITY` - Esperando creación de identidad en Keycloak
- `ACTIVE` - Estudiante activo con identidad creada
- `DISABLED` - Estudiante deshabilitado

**Errores posibles:**

- `404 Not Found` - `CourseNotFoundException`: El curso no existe
- `400 Bad Request` - `CourseNotInUpcomingTermException`: El curso no pertenece a un periodo académico con estado
  UPCOMING

---

### Listar Todos los Estudiantes

```
GET /api/students
```

**Descripción:** Obtiene la lista de todos los estudiantes registrados.

**Response:** `200 OK`

```json
[
  {
    "id": "string (UUID)",
    "firstName": "string",
    "lastName": "string",
    "courseId": "string (UUID)",
    "status": "PENDING_IDENTITY | ACTIVE | DISABLED",
    "userId": "string | null"
  }
]
```

---

### Actualizar Estudiante

```
PUT /api/students/{id}
```

**Descripción:** Actualiza los datos de un estudiante existente, incluyendo la posibilidad de cambiar de curso.

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|-----------------------|
| id | string | UUID del estudiante |

**Request Body:**

```json
{
  "firstName": "string",
  "lastName": "string",
  "courseId": "string (UUID)"
}
```

**Response:** `200 OK`

```json
{
  "id": "string (UUID)",
  "firstName": "string",
  "lastName": "string",
  "courseId": "string (UUID)",
  "status": "PENDING_IDENTITY | ACTIVE | DISABLED",
  "userId": "string | null"
}
```

**Errores posibles:**

- `404 Not Found` - `StudentNotFoundException`: El estudiante no existe
- `400 Bad Request` - `StudentNotActiveException`: Solo se pueden modificar estudiantes con estado ACTIVE

---

### Deshabilitar Estudiante

```
DELETE /api/students/{id}
```

**Descripción:** Deshabilita un estudiante y su identidad en Keycloak. Este es un proceso asíncrono que utiliza el
patrón Saga para coordinar la deshabilitación local y en el proveedor de identidad.

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|-----------------------|
| id | string | UUID del estudiante |

**Response:** `202 Accepted`

**Nota:** La respuesta 202 indica que la solicitud fue aceptada y el proceso de deshabilitación se ejecutará de forma
asíncrona.

**Errores posibles:**

- `404 Not Found` - `StudentNotFoundException`: El estudiante no existe
- `400 Bad Request` - `StudentAlreadyDisabledException`: El estudiante ya está deshabilitado
- `400 Bad Request` - `StudentNotActiveException`: Solo se pueden deshabilitar estudiantes con estado ACTIVE

---

## Módulo Assignments (Tasks)

Base path: `/api/tasks`

### Crear Tarea

```
POST /api/tasks
```

**Descripción:** Crea una nueva tarea (assignment) para un curso. Si no es histórica, el curso debe pertenecer a un
periodo académico con estado UPCOMING.

**Request Body:**

```json
{
  "title": "string",
  "summary": "string",
  "deadline": "YYYY-MM-DDTHH:mm:ss",
  "minScoreToPass": "number (0-100)",
  "courseId": "string (UUID)",
  "maxAttempts": "number (integer, min 1)",
  "historical": "boolean (optional, default false)"
}
```

**Response:** `201 Created`

```json
{
  "id": "string (UUID)",
  "title": "string",
  "summary": "string",
  "deadline": "YYYY-MM-DDTHH:mm:ss",
  "minScoreToPass": "number",
  "maxAttempts": "number",
  "courseId": "string (UUID)",
  "status": "NOT_STARTED | IN_PROGRESS | FINISHED"
}
```

**Estados de la tarea:**

- `NOT_STARTED` - La tarea aún no ha comenzado (deadline en el futuro)
- `IN_PROGRESS` - La tarea está actualmente activa
- `FINISHED` - La tarea ha finalizado (deadline pasado)

**Nota:** El estado se actualiza automáticamente mediante el `TaskStatusScheduler`.

**Errores posibles:**

- `404 Not Found` - `CourseNotFoundException`: El curso no existe
- `400 Bad Request` - `CourseNotInUpcomingTermException`: El curso no pertenece a un periodo académico con estado
  UPCOMING (solo aplica si `historical` es false)

---

### Listar Todas las Tareas

```
GET /api/tasks
```

**Descripción:** Obtiene la lista de todas las tareas registradas.

**Response:** `200 OK`

```json
[
  {
    "id": "string (UUID)",
    "title": "string",
    "summary": "string",
    "deadline": "YYYY-MM-DDTHH:mm:ss",
    "minScoreToPass": "number",
    "maxAttempts": "number",
    "courseId": "string (UUID)",
    "status": "NOT_STARTED | IN_PROGRESS | FINISHED"
  }
]
```

---

## Módulo Scenarios

Base path: `/api/scenarios`

### Crear Escenario

```
POST /api/scenarios
```

**Descripción:** Crea un nuevo escenario de videojuego con toda su configuración (dimensiones, consumidor, acciones,
eventos).

**Request Body:**

```json
{
  "name": "string",
  "description": "string",
  "dimensions": [
    {
      "name": "string",
      "defaultValue": "number"
    }
  ],
  "consumer": {
    "name": "string",
    "dimensions": [
      {
        "name": "string",
        "defaultValue": "number"
      }
    ]
  },
  "actions": [
    {
      "name": "string",
      "description": "string",
      "cooldownInSeconds": "number",
      "effects": [
        {
          "dimensionName": "string",
          "effectValue": "number",
          "target": "PLAYER | CONSUMER"
        }
      ]
    }
  ],
  "events": [
    {
      "name": "string",
      "description": "string",
      "probability": "number (0.0-1.0)",
      "effects": [
        {
          "dimensionName": "string",
          "effectValue": "number",
          "target": "PLAYER | CONSUMER"
        }
      ]
    }
  ]
}
```

**Response:** `201 Created`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "description": "string",
  "dimensions": [
    ...
  ],
  "consumer": {
    ...
  },
  "actions": [
    ...
  ],
  "events": [
    ...
  ]
}
```

---

### Obtener Escenario por ID

```
GET /api/scenarios/{id}
```

**Descripción:** Obtiene los detalles completos de un escenario específico.

**Parámetros de ruta:**
| Parámetro | Tipo | Descripción |
|-----------|--------|----------------------|
| id | string | UUID del escenario |

**Response:** `200 OK`

```json
{
  "id": "string (UUID)",
  "name": "string",
  "description": "string",
  "dimensions": [
    {
      "id": "string (UUID)",
      "name": "string",
      "defaultValue": "number"
    }
  ],
  "consumer": {
    "id": "string (UUID)",
    "name": "string",
    "dimensions": [
      {
        "id": "string (UUID)",
        "name": "string",
        "defaultValue": "number"
      }
    ]
  },
  "actions": [
    {
      "id": "string (UUID)",
      "name": "string",
      "description": "string",
      "cooldownInSeconds": "number",
      "effects": [
        {
          "id": "string (UUID)",
          "dimensionName": "string",
          "effectValue": "number",
          "target": "PLAYER | CONSUMER"
        }
      ]
    }
  ],
  "events": [
    {
      "id": "string (UUID)",
      "name": "string",
      "description": "string",
      "probability": "number",
      "effects": [
        {
          "id": "string (UUID)",
          "dimensionName": "string",
          "effectValue": "number",
          "target": "PLAYER | CONSUMER"
        }
      ]
    }
  ]
}
```

**Errores posibles:**

- `404 Not Found` - `ScenarioNotFoundException`: El escenario con ID especificado no existe

---

### Listar Todos los Escenarios

```
GET /api/scenarios
```

**Descripción:** Obtiene la lista de todos los escenarios registrados.

**Response:** `200 OK`

```json
[
  {
    "id": "string (UUID)",
    "name": "string",
    "description": "string",
    "dimensions": [
      ...
    ],
    "consumer": {
      ...
    },
    "actions": [
      ...
    ],
    "events": [
      ...
    ]
  }
]
```

---

## Códigos de Estado HTTP

| Código | Descripción                                                |
|--------|------------------------------------------------------------|
| 200    | OK - Solicitud exitosa                                     |
| 201    | Created - Recurso creado exitosamente                      |
| 202    | Accepted - Solicitud aceptada para procesamiento asíncrono |
| 400    | Bad Request - Error de validación o regla de negocio       |
| 404    | Not Found - Recurso no encontrado                          |
| 500    | Internal Server Error - Error interno del servidor         |

---

## Excepciones de Dominio

### Módulo Academic Terms

| Excepción                          | Descripción                                                    |
|------------------------------------|----------------------------------------------------------------|
| `AcademicTermNotFoundException`    | El periodo académico solicitado no existe                      |
| `AcademicTermNotUpcomingException` | La operación requiere un periodo académico con estado UPCOMING |
| `InvalidStatusTransitionException` | Transición de estado no permitida                              |

### Módulo Courses

| Excepción                                | Descripción                                |
|------------------------------------------|--------------------------------------------|
| `CourseNotFoundException`                | El curso solicitado no existe              |
| `InvalidCourseStatusTransitionException` | Transición de estado de curso no permitida |

### Módulo Students

| Excepción                                 | Descripción                                                       |
|-------------------------------------------|-------------------------------------------------------------------|
| `StudentNotFoundException`                | El estudiante solicitado no existe                                |
| `StudentAlreadyDisabledException`         | El estudiante ya se encuentra deshabilitado                       |
| `StudentNotActiveException`               | La operación requiere un estudiante con estado ACTIVE             |
| `CourseNotInUpcomingTermException`        | El curso no pertenece a un periodo académico UPCOMING             |
| `UserNotFoundInIdentityProviderException` | No se encontró el usuario en el proveedor de identidad (Keycloak) |

### Módulo Assignments

| Excepción                          | Descripción                                           |
|------------------------------------|-------------------------------------------------------|
| `CourseNotInUpcomingTermException` | El curso no pertenece a un periodo académico UPCOMING |

### Módulo Scenarios

| Excepción                   | Descripción                       |
|-----------------------------|-----------------------------------|
| `ScenarioNotFoundException` | El escenario solicitado no existe |

---

## Patrones de Integración

### Saga Pattern (Keycloak Integration)

El módulo de estudiantes utiliza el patrón Saga con coreografía basada en eventos para coordinar operaciones con
Keycloak:

**Registro de Estudiante:**

1. Se crea el estudiante localmente con estado `PENDING_IDENTITY`
2. Se publica `StudentRegisteredEvent`
3. El listener crea la identidad en Keycloak
4. Si es exitoso, se publica `UserIdentityCreatedEvent` → estado cambia a `ACTIVE`
5. Si falla, se publica `UserIdentityCreationFailedEvent` → se puede reintentar

**Deshabilitación de Estudiante:**

1. Se valida que el estudiante esté `ACTIVE`
2. Se publica `StudentDisableRequestedEvent`
3. El listener deshabilita localmente y en Keycloak
4. Si es exitoso, se publica `UserDisabledEvent` → estado cambia a `DISABLED`
5. Si falla, se ejecuta rollback y se publica `UserDisableFailedEvent`
