# Documentación de Tests del Dominio

## Propósito

Este documento describe los tests unitarios de la capa de dominio del proyecto MarkenX API. Los tests del dominio verifican las reglas de negocio, invariantes de agregados, comportamiento de Value Objects y transiciones de estado sin depender de infraestructura externa.

### Principios de Testing Aplicados

- **Sin dependencias de Spring**: Los tests no cargan el contexto de Spring (`@SpringBootTest`, `@ContextConfiguration` no se utilizan).
- **Sin mocks**: La lógica del dominio es pura y no requiere mocks de repositorios o servicios externos.
- **Instanciación directa**: Los objetos se crean directamente usando constructores y factories.
- **JUnit 5 + AssertJ**: Se utiliza JUnit Jupiter para las aserciones y AssertJ para validaciones fluidas.
- **Nomenclatura**: `givenX_whenY_thenZ` para claridad en el comportamiento esperado.

---

## Value Objects

### AssignmentInfo

Tests ubicados en: `classroom/assignments/domain/models/valueobjects/AssignmentInfoTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidTitleAndSummary_whenCreatingAssignmentInfo_thenSucceeds` | Creación exitosa con datos válidos |
| `givenNullTitle_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentTitleException` | El título no puede ser nulo |
| `givenBlankTitle_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentTitleException` | El título no puede estar en blanco |
| `givenNullSummary_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentDescriptionException` | El resumen no puede ser nulo |
| `givenBlankSummary_whenCreatingAssignmentInfo_thenThrowsInvalidAssignmentDescriptionException` | El resumen no puede estar en blanco |

**Criterios de Aceptación:**

- **DADO** un título y resumen válidos, **CUANDO** se crea un AssignmentInfo, **ENTONCES** se almacenan correctamente ambos valores.
- **DADO** un título nulo o en blanco, **CUANDO** se intenta crear un AssignmentInfo, **ENTONCES** se lanza `InvalidAssignmentTitleException`.
- **DADO** un resumen nulo o en blanco, **CUANDO** se intenta crear un AssignmentInfo, **ENTONCES** se lanza `InvalidAssignmentDescriptionException`.

---

### AssignmentScore

Tests ubicados en: `classroom/assignments/domain/models/valueobjects/AssignmentScoreTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidScore_whenCreatingAssignmentScore_thenSucceeds` | Scores entre 0.0 y 1.0 son válidos |
| `givenNaNScore_whenCreatingAssignmentScore_thenThrowsScoreIsNotANumberException` | NaN no es un score válido |
| `givenNegativeScore_whenCreatingAssignmentScore_thenThrowsScoreOutOfAllowedRangeException` | Scores negativos no son válidos |
| `givenScoreGreaterThanOne_whenCreatingAssignmentScore_thenThrowsScoreOutOfAllowedRangeException` | Scores mayores a 1.0 no son válidos |
| `givenHigherScore_whenComparingWithLowerScore_thenIsGreaterOrEqual` | Comparación entre scores |

**Criterios de Aceptación:**

- **DADO** un valor entre 0.0 y 1.0, **CUANDO** se crea un AssignmentScore, **ENTONCES** la creación es exitosa.
- **DADO** un valor fuera del rango [0.0, 1.0], **CUANDO** se intenta crear un AssignmentScore, **ENTONCES** se lanza `ScoreOutOfAllowedRangeException`.
- **DADO** un valor NaN, **CUANDO** se intenta crear un AssignmentScore, **ENTONCES** se lanza `ScoreIsNotANumberException`.

---

### AssignmentDeadline

Tests ubicados en: `classroom/assignments/domain/models/valueobjects/AssignmentDeadlineTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenFutureDateTime_whenCreatingFutureDeadline_thenSucceeds` | Fechas futuras son válidas para deadlines nuevos |
| `givenPastDateTime_whenCreatingFutureDeadline_thenThrowsDueDateMustBeInTheFutureException` | Fechas pasadas no son válidas para nuevos deadlines |
| `givenPastDateTime_whenCreatingHistoricalDeadline_thenSucceeds` | Fechas pasadas son válidas para datos históricos |
| `givenDeadline_whenCheckingIsOverdue_thenReturnsCorrectValue` | Verificación de vencimiento |

**Criterios de Aceptación:**

- **DADO** una fecha futura, **CUANDO** se crea un deadline con `future()`, **ENTONCES** la creación es exitosa.
- **DADO** una fecha pasada, **CUANDO** se intenta crear un deadline con `future()`, **ENTONCES** se lanza `DueDateMustBeInTheFutureException`.
- **DADO** cualquier fecha, **CUANDO** se crea un deadline con `historical()`, **ENTONCES** la creación es exitosa (para reconstrucción de datos).

---

### PersonName

Tests ubicados en: `classroom/students/domain/models/valueobjects/PersonNameTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidName_whenCreatingPersonName_thenSucceeds` | Nombres válidos se crean correctamente |
| `givenNullName_whenCreatingPersonName_thenThrowsPersonNameCannotBeEmptyException` | Nombres nulos no son válidos |
| `givenNameWithMultipleInternalSpaces_whenCreating_thenNormalizesToSingleSpace` | Normalización de espacios múltiples |
| `givenTwoPersonNamesWithDifferentCase_whenComparing_thenAreEqual` | Comparación case-insensitive |

**Criterios de Aceptación:**

- **DADO** un nombre válido, **CUANDO** se crea un PersonName, **ENTONCES** el valor se normaliza (trim, espacios simples).
- **DADO** un nombre nulo o vacío, **CUANDO** se intenta crear un PersonName, **ENTONCES** se lanza `PersonNameCannotBeEmptyException`.
- **DADO** dos nombres con diferente capitalización, **CUANDO** se comparan, **ENTONCES** son iguales.

---

### Email

Tests ubicados en: `security/domain/models/valueobjects/EmailTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidEmailWithAllowedDomain_whenCreating_thenSucceeds` | Emails con dominios permitidos son válidos |
| `givenInvalidEmailFormat_whenCreating_thenThrowsInvalidEmailFormatException` | Formato de email inválido |
| `givenEmailWithNotAllowedDomain_whenCreating_thenThrowsEmailDomainNotAllowedException` | Dominios no permitidos son rechazados |
| `givenEmailWithUpperCase_whenCreating_thenNormalizesToLowerCase` | Normalización a minúsculas |

**Criterios de Aceptación:**

- **DADO** un email con formato válido y dominio permitido, **CUANDO** se crea un Email, **ENTONCES** la creación es exitosa y se normaliza a minúsculas.
- **DADO** un email con formato inválido, **CUANDO** se intenta crear un Email, **ENTONCES** se lanza `InvalidEmailFormatException`.
- **DADO** un email con dominio no permitido, **CUANDO** se intenta crear un Email, **ENTONCES** se lanza `EmailDomainNotAllowedException`.

---

### DateInterval

Tests ubicados en: `classroom/terms/domain/models/aggregates/DateIntervalTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidDates_whenCreatingDateInterval_thenSucceeds` | Intervalos válidos se crean correctamente |
| `givenEndDateBeforeStartDate_whenCreatingDateInterval_thenThrowsInvalidDateIntervalException` | Fecha fin debe ser posterior a fecha inicio |
| `givenTwoOverlappingIntervals_whenCheckingOverlap_thenReturnsTrue` | Detección de solapamiento |
| `givenDateWithinInterval_whenCheckingContains_thenReturnsTrue` | Verificación de contenido de fechas |

**Criterios de Aceptación:**

- **DADO** fechas donde inicio < fin, **CUANDO** se crea un DateInterval, **ENTONCES** la creación es exitosa.
- **DADO** fechas donde fin < inicio, **CUANDO** se intenta crear un DateInterval, **ENTONCES** se lanza `InvalidDateIntervalException`.
- **DADO** dos intervalos que se solapan, **CUANDO** se verifica el solapamiento, **ENTONCES** retorna `true`.

---

### AttemptResult

Tests ubicados en: `game/attempts/domain/models/valueobjects/AttemptResultTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidValues_whenCreatingAttemptResult_thenSucceeds` | Resultados válidos se crean correctamente |
| `givenZeroTurn_whenCreating_thenThrowsCurrentTurnMustBePositiveException` | El turno debe ser positivo |
| `givenNegativeBudget_whenCreating_thenThrowsBudgetCannotBeNegativeException` | El presupuesto no puede ser negativo |
| `givenApprovalRateOutOfRange_whenCreating_thenThrowsApprovalRateOutOfRangeException` | Tasa de aprobación debe estar en [0,1] |

**Criterios de Aceptación:**

- **DADO** valores válidos para turno, presupuesto, tasa de aprobación y score de perfil, **CUANDO** se crea un AttemptResult, **ENTONCES** la creación es exitosa.
- **DADO** un turno <= 0, **CUANDO** se intenta crear un AttemptResult, **ENTONCES** se lanza `CurrentTurnMustBePositiveException`.
- **DADO** un presupuesto negativo, **CUANDO** se intenta crear un AttemptResult, **ENTONCES** se lanza `BudgetCannotBeNegativeException`.

---

### StudentStatus y AttemptStatus (Enums con Transiciones)

Tests ubicados en:
- `classroom/students/domain/models/valueobjects/StudentStatusTest.java`
- `game/attempts/domain/models/valueobjects/AttemptStatusTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenPendingIdentityStatus_whenTransitioningToActive_thenIsAllowed` | Transición válida de PENDING_IDENTITY a ACTIVE |
| `givenActiveStatus_whenTransitioningToAnyStatus_thenIsNotAllowed` | ACTIVE es un estado terminal |
| `givenUnknownStatus_whenTransitioningToApproved_thenIsAllowed` | Transición válida de UNKNOWN a APPROVED |

**Criterios de Aceptación:**

- **DADO** un estado inicial válido, **CUANDO** se transiciona a un estado permitido, **ENTONCES** la transición es aceptada.
- **DADO** un estado terminal, **CUANDO** se intenta transicionar a cualquier estado, **ENTONCES** la transición es rechazada.

---

## Agregados

### Task

Tests ubicados en: `classroom/assignments/domain/models/aggregates/TaskTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidParameters_whenCreatingTask_thenSucceeds` | Creación exitosa de tarea |
| `givenPastDeadline_whenCreatingTask_thenThrowsDueDateMustBeInTheFutureException` | Deadline debe ser futuro |
| `givenZeroMaxAttempts_whenCreatingTask_thenThrowsInvalidMaxAttemptsException` | Intentos máximos deben ser positivos |
| `givenNullScenarioId_whenCreatingTask_thenThrowsInvalidScenarioIdException` | Escenario es requerido |

**Criterios de Aceptación:**

- **DADO** parámetros válidos (info, deadline futuro, score, curso, intentos, escenario), **CUANDO** se crea una Task, **ENTONCES** se genera un ID único y el estado es ACTIVE.
- **DADO** un deadline en el pasado, **CUANDO** se intenta crear una Task, **ENTONCES** se lanza `DueDateMustBeInTheFutureException`.
- **DADO** maxAttempts <= 0, **CUANDO** se intenta crear una Task, **ENTONCES** se lanza `InvalidMaxAttemptsException`.

---

### Student

Tests ubicados en: `classroom/students/domain/models/aggregates/StudentTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidData_whenCreatingStudent_thenSucceeds` | Creación exitosa con estado PENDING_IDENTITY |
| `givenPendingIdentityStudent_whenMarkingIdentityCreated_thenTransitionsToActive` | Transición de estado tras crear identidad |
| `givenActiveStudent_whenMarkingIdentityCreated_thenThrowsInvalidStudentStatusTransitionException` | Transición inválida desde ACTIVE |
| `givenStudent_whenAssigningUser_thenUserIdIsSet` | Asignación de usuario |

**Criterios de Aceptación:**

- **DADO** nombre, apellido y curso válidos, **CUANDO** se crea un Student, **ENTONCES** el estado inicial es PENDING_IDENTITY y no tiene userId.
- **DADO** un estudiante en PENDING_IDENTITY, **CUANDO** se marca la identidad como creada, **ENTONCES** transiciona a ACTIVE.
- **DADO** un estudiante en ACTIVE, **CUANDO** se intenta marcar la identidad como creada nuevamente, **ENTONCES** se lanza `InvalidStudentStatusTransitionException`.

---

### Term

Tests ubicados en: `classroom/terms/domain/models/aggregates/TermTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidFutureSingleYearTerm_whenCreating_thenSucceeds` | Período académico futuro válido |
| `givenStartDateInPast_whenCreating_thenThrowsTermMustStartInFutureException` | Fecha inicio debe ser futura |
| `givenTermTooShort_whenCreating_thenThrowsInvalidTermLengthException` | Duración mínima de 4 meses |
| `givenTwoOverlappingTerms_whenCheckingOverlap_thenReturnsTrue` | Detección de solapamiento entre períodos |

**Criterios de Aceptación:**

- **DADO** un período con fecha inicio futura y duración entre 4-6 meses, **CUANDO** se crea un Term, **ENTONCES** la creación es exitosa.
- **DADO** una fecha de inicio en el pasado, **CUANDO** se intenta crear un Term, **ENTONCES** se lanza `TermMustStartInFutureException`.
- **DADO** una duración menor a 4 meses o mayor a 6 meses, **CUANDO** se intenta crear un Term, **ENTONCES** se lanza `InvalidTermLengthException`.
- **DADO** dos períodos que se solapan, **CUANDO** se verifica el solapamiento, **ENTONCES** retorna `true`.

---

### Attempt

Tests ubicados en: `game/attempts/domain/models/aggregates/AttemptTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidData_whenCreatingAttempt_thenSucceeds` | Creación exitosa con estado UNKNOWN |
| `givenPassingScore_whenCreatingWithResults_thenStatusIsApproved` | Determinación automática de estado APPROVED |
| `givenUnknownStatusAttempt_whenRegisteringPassingResults_thenTransitionsToApproved` | Registro de resultados con transición a APPROVED |
| `givenUnknownStatusAttempt_whenRegisteringFailingResults_thenTransitionsToDisapproved` | Registro de resultados con transición a DISAPPROVED |
| `givenUnknownStatusAttempt_whenRegisteringExactMinScore_thenTransitionsToApproved` | Score exacto al mínimo es APPROVED |
| `givenAttemptWithResults_whenRegisteringResultsAgain_thenThrowsResultsAlreadyRegisteredException` | No se pueden registrar resultados dos veces |
| `givenUnknownStatusAttempt_whenRegisteringNullResult_thenThrowsNullAttemptResultException` | Resultado nulo no permitido |

**Criterios de Aceptación:**

- **DADO** un taskId y studentId válidos, **CUANDO** se crea un Attempt, **ENTONCES** el estado inicial es UNKNOWN y no tiene resultados.
- **DADO** un intento con estado UNKNOWN, **CUANDO** se registran resultados con aprobación >= minScore, **ENTONCES** transiciona a APPROVED.
- **DADO** un intento con estado UNKNOWN, **CUANDO** se registran resultados con aprobación < minScore, **ENTONCES** transiciona a DISAPPROVED.
- **DADO** un intento con resultados ya registrados, **CUANDO** se intenta registrar resultados nuevamente, **ENTONCES** se lanza `ResultsAlreadyRegisteredException`.
- **DADO** un intento sin resultados, **CUANDO** se intenta registrar un resultado nulo, **ENTONCES** se lanza `NullAttemptResultException`.

---

### User

Tests ubicados en: `security/domain/models/aggregates/UserTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidEmailAndRole_whenCreatingUser_thenSucceeds` | Creación exitosa de usuario |
| `givenUser_whenUpdatingEmail_thenEmailIsUpdated` | Actualización de email |
| `givenActiveUser_whenDisabling_thenUserIsDisabled` | Deshabilitación de usuario |
| `givenDisabledUser_whenDisabling_thenThrowsEntityAlreadyDisabledException` | No se puede deshabilitar usuario ya deshabilitado |

**Criterios de Aceptación:**

- **DADO** un email válido y un rol, **CUANDO** se crea un User, **ENTONCES** se genera un ID único y el estado es ACTIVE.
- **DADO** un usuario activo, **CUANDO** se deshabilita, **ENTONCES** el estado cambia a DISABLED.
- **DADO** un usuario deshabilitado, **CUANDO** se intenta deshabilitar nuevamente, **ENTONCES** se lanza `EntityAlreadyDisabledException`.

---

## Clases Base Compartidas

### Identifier

Tests ubicados en: `shared/domain/models/aggregates/IdentifierTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenValidValue_whenCreatingIdentifier_thenSucceeds` | Creación exitosa con valor válido |
| `givenNullValue_whenCreatingIdentifier_thenThrowsIdentifierCannotBeNullException` | Valor nulo no permitido |
| `givenBlankValue_whenCreatingIdentifier_thenThrowsIdentifierCannotBeNullException` | Valor en blanco no permitido |

**Criterios de Aceptación:**

- **DADO** un valor no nulo y no vacío, **CUANDO** se crea un Identifier, **ENTONCES** la creación es exitosa.
- **DADO** un valor nulo o vacío, **CUANDO** se intenta crear un Identifier, **ENTONCES** se lanza `IdentifierCannotBeNullException`.

---

### Entity

Tests ubicados en: `shared/domain/models/aggregates/EntityTest.java`

| Test | Comportamiento |
|------|----------------|
| `givenDefaultConstructor_whenCreatingEntity_thenStatusIsActive` | Estado inicial es ACTIVE |
| `givenActiveEntity_whenDisabling_thenEntityIsDisabled` | Transición a DISABLED |
| `givenDisabledEntity_whenEnabling_thenEntityIsActive` | Transición a ACTIVE |
| `givenDisabledEntity_whenDisabling_thenThrowsEntityAlreadyDisabledException` | Operación idempotente rechazada |

**Criterios de Aceptación:**

- **DADO** una entidad nueva, **CUANDO** se crea con el constructor por defecto, **ENTONCES** el estado es ACTIVE.
- **DADO** una entidad activa, **CUANDO** se deshabilita, **ENTONCES** el estado cambia a DISABLED.
- **DADO** una entidad deshabilitada, **CUANDO** se intenta deshabilitar, **ENTONCES** se lanza `EntityAlreadyDisabledException`.

---

## Lo Que NO Se Testea y Por Qué

### Eventos de Dominio (Domain Events)

Los eventos de dominio (`StudentRegisteredEvent`, `ScenarioCreatedEvent`, etc.) **no se testean directamente** porque:

1. **Son registros inmutables (records)**: No contienen lógica de negocio, solo transportan datos.
2. **La emisión de eventos depende de infraestructura**: La publicación real de eventos requiere `ApplicationEventPublisher` de Spring.
3. **Se testean indirectamente**: Los handlers de eventos pertenecen a la capa de aplicación y se testean con tests de integración.

### Servicios de Dominio que Requieren Puertos

Los servicios de dominio como `AcademicTermDomainService` o `UserDomainService` que dependen de puertos/repositorios **no se testean aquí** porque:

1. **Requieren colaboradores externos**: Necesitan implementaciones de repositorios.
2. **Pertenecen a tests de integración**: Se testean en conjunto con la capa de aplicación.

### Mappers e Infraestructura

- Los mappers (DTOs ↔ Entities) no pertenecen al dominio.
- Los repositorios y adaptadores pertenecen a la capa de infraestructura.
- Los controladores REST pertenecen a la capa de infraestructura.

---

## Estructura de Archivos de Test

```
src/test/java/com/udla/markenx/api/
├── classroom/
│   ├── assignments/
│   │   └── domain/
│   │       └── models/
│   │           ├── aggregates/
│   │           │   └── TaskTest.java
│   │           └── valueobjects/
│   │               ├── AssignmentInfoTest.java
│   │               ├── AssignmentScoreTest.java
│   │               └── AssignmentDeadlineTest.java
│   ├── students/
│   │   └── domain/
│   │       └── models/
│   │           ├── aggregates/
│   │           │   └── StudentTest.java
│   │           └── valueobjects/
│   │               ├── PersonNameTest.java
│   │               └── StudentStatusTest.java
│   └── terms/
│       └── domain/
│           └── models/
│               └── aggregates/
│                   ├── DateIntervalTest.java
│                   └── TermTest.java
├── game/
│   └── attempts/
│       └── domain/
│           └── models/
│               ├── aggregates/
│               │   └── AttemptTest.java
│               └── valueobjects/
│                   ├── AttemptResultTest.java
│                   └── AttemptStatusTest.java
├── security/
│   └── domain/
│       └── models/
│           ├── aggregates/
│           │   └── UserTest.java
│           └── valueobjects/
│               └── EmailTest.java
└── shared/
    └── domain/
        └── models/
            ├── aggregates/
            │   ├── EntityTest.java
            │   └── IdentifierTest.java
            └── valueobjects/
                └── LifecycleStatusTest.java
```

---

## Ejecución de Tests

Para ejecutar solo los tests del dominio:

```bash
mvn test -Dtest="**/*Test.java"
```

Para generar reporte de cobertura:

```bash
mvn test jacoco:report
```

El reporte estará disponible en `target/site/jacoco/index.html`.
