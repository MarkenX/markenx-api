"""
Actúa como Senior Software Architect + Backend Engineer experto en Spring Boot (Java 21, Maven), DDD, Arquitectura Hexagonal, Spring Modulith, Saga Pattern, JDBC (Commands), jOOQ (Queries), MySQL, Flyway, Keycloak y CI/CD con CircleCI.

Tienes acceso completo al repositorio, a la carpeta docs/ y a todos los módulos ya creados en la API MarkenX.

Contexto general

La API está prácticamente finalizada. Todos los módulos existen y siguen las convenciones del proyecto. El objetivo de este trabajo es completar la API implementando el módulo faltante Attempts, actualizar documentación y generar el README final del proyecto, teniendo en cuenta ejecución en DEV y el flujo de CI/CD existente.

FASE 1 – Análisis previo obligatorio

Antes de implementar cualquier cambio:

Analiza el estado actual del módulo Attempts. El dominio ya está implementado y debe respetarse sin rediseños innecesarios.

Analiza cómo los demás módulos exponen endpoints POST y GET, cómo estructuran commands, queries, application services, adapters REST y repositorios JDBC/jOOQ.

Revisa la documentación existente en docs/, especialmente la documentación de endpoints y base de datos ya generada.

Analiza el pipeline de CI en CircleCI y entiende cómo se ejecuta el proyecto en entorno DEV. Ten en cuenta que el CD se gestiona desde el repositorio markenx-config.

No implementes cambios hasta comprender completamente estos flujos.

FASE 2 – Módulo Attempts: POST de resultados de partida

Se requiere implementar en el módulo Attempts un endpoint POST que permita cargar los resultados de una partida de videojuego.

El endpoint debe aceptar un JSON equivalente a la siguiente estructura lógica. El campo finalOutcome puede omitirse, ya que el resultado de la partida se calcula internamente en el módulo Attempts comparando el profileDiscoveryPercentage con el minScoreToPass de la Asignación correspondiente.

GameSessionReport:
- sessionDate
- finalAcceptance
- remainingBudget
- totalTurnsUsed
- profileDiscoveryPercentage
- history

TurnHistoryLog:
- turnNumber
- acceptanceAtEnd
- budgetAtEnd
- actionsTakenIds
- eventOcurredTitle

Ejemplo del JSON que se recibe para registrar los resultados de una partida:

{
"sessionDate": "2026-01-07 14:32:10",
"finalAcceptance": 0.83,
"remainingBudget": 120,
"totalTurnsUsed": 5,
"profileDiscoveryPercentage": 0.75,
"history": [
{
"turnNumber": 1,
"budgetAtEnd": 180,
"acceptanceAtEnd": 0.60,
"eventOcurredTitle": "",
"actionsTakenIds": [
"3f8a9bde-1c2f-4a6b-9d2f-0a1b2c3d4e5f",
"7a4c2d1e-9b8f-4c3a-8d7e-2f1e0d9c8b7a"
]
},
{
"turnNumber": 2,
"budgetAtEnd": 140,
"acceptanceAtEnd": 0.68,
"eventOcurredTitle": "Protesta ciudadana",
"actionsTakenIds": [
"9d7e6f5a-3b2c-4d1e-8f9a-0b1c2d3e4f5a"
]
},
{
"turnNumber": 3,
"budgetAtEnd": 100,
"acceptanceAtEnd": 0.74,
"eventOcurredTitle": "",
"actionsTakenIds": [
"2b6f4a9c-5d3e-4f1a-8c7b-0e9d8c7b6a5f",
"5c3a1b2d-8f6e-4d9c-7b2a-1e0d9c8b7a6f"
]
},
{
"turnNumber": 4,
"budgetAtEnd": 80,
"acceptanceAtEnd": 0.80,
"eventOcurredTitle": "",
"actionsTakenIds": [
"c1d2e3f4-5a6b-7c8d-9e0f-1a2b3c4d5e6f"
]
},
{
"turnNumber": 5,
"budgetAtEnd": 120,
"acceptanceAtEnd": 0.83,
"eventOcurredTitle": "Campaña exitosa",
"actionsTakenIds": [
"f6e5d4c3-b2a1-0f9e-8d7c-6b5a4c3d2e1f"
]
}
]
}

Consideraciones obligatorias:
- El JSON se recibe como DTO y se transforma a commands de aplicación
- El cálculo de finalOutcome pertenece al dominio del módulo Attempts
- La lógica de negocio debe residir en el dominio y application services
- La persistencia debe seguir el patrón JDBC para commands
- Se deben validar invariantes de dominio y lanzar excepciones de dominio cuando corresponda
- El endpoint debe seguir el estilo y convenciones de los demás módulos

FASE 3 – Módulo Attempts: GET de resultados de partida

Se requiere implementar un endpoint GET que permita obtener toda la información de una partida en un solo JSON.

Consideraciones:
- El JSON de salida debe mantener la misma estructura que el POST, incluyendo el finalOutcome ya calculado
- La información debe obtenerse usando queries basadas en jOOQ
- El ensamblado del JSON debe realizarse en la capa de aplicación o adapter, no en el dominio
- El endpoint debe permitir identificar la partida por su identificador correspondiente según el dominio de Attempts

FASE 4 – Actualización de documentación de endpoints

Se debe actualizar la documentación de la API para incluir los endpoints del módulo Attempts.

La documentación debe:
- Guardarse en docs/api/endpoints.md
- Incluir los endpoints POST y GET de Attempts
- Especificar que finalOutcome es un campo calculado y no requerido en el POST
- Describir método HTTP, path, propósito, request, response y errores de dominio
- Mantener coherencia con el resto de la documentación existente

FASE 5 – Generación del README.md final del proyecto

Se debe generar un README.md en la raíz del repositorio de la API, utilizando como base la estructura y contenido proporcionados, adaptándolos correctamente al contexto del proyecto.

El README debe:
- Describir el sistema MarkenX y sus componentes
- Listar tecnologías, prerequisitos y pasos de despliegue
- Explicar acceso a ArgoCD y a las aplicaciones
- Describir la estructura del repositorio markenx-config
- Listar comandos disponibles
- Incluir enlaces a la documentación generada en docs/:
  - docs/api/endpoints.md
  - docs/database/tables.md
  - docs/database/chart.md
- Incluir una sección clara de ejecución en entorno DEV
- Incluir una sección de CI/CD:
  - CI con CircleCI en este repositorio
  - CD gestionado desde el repositorio externo https://github.com/MarkenX/markenx-config

FASE 6 – Entregables finales

Implementa los cambios solicitados respetando estrictamente:
- DDD
- Arquitectura Hexagonal
- Spring Modulith
- Separación entre dominio, aplicación e infraestructura
- JDBC para commands y jOOQ para queries
- Convenciones existentes del proyecto

Entrega:
- Endpoints POST y GET completos para el módulo Attempts
- Commands, queries y application services necesarios
- Ajustes de persistencia JDBC y jOOQ
- Actualización de docs/api/endpoints.md
- README.md completo y finalizado con enlaces a la documentación
- Coherencia total con ejecución DEV y pipeline de CI/CD existente

Reglas estrictas finales

No introducir JPA.
No mover lógica a controllers.
No romper límites entre módulos.
No introducir dependencias circulares.
No modificar innecesariamente dominios ya implementados.
Usar excepciones de dominio.
Respetar el estilo y convenciones del repositorio.

Antes de implementar, explica brevemente cómo entiendes el módulo Attempts, el flujo POST/GET de resultados de partida y el impacto de estos cambios en la API.
"""
