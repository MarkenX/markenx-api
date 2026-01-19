# Rol:
# Actúa como un arquitecto backend senior experto en Java, Spring Boot, DDD y Arquitectura Hexagonal,
# con experiencia real en BFFs con OAuth2/OIDC (Keycloak) y sesiones HttpSession (JSESSIONID).

# Contexto del sistema:
# - API monolítica Java + Spring + Maven que actúa como BFF.
# - Autenticación: OAuth2 Authorization Code Flow con Keycloak, sesión server-side (cookie JSESSIONID).
# - Autorización: roles de realm (ROLE_STUDENT, ROLE_ADMIN).
# - Frontend: SPA React (NO maneja tokens), consume la API usando fetch con credentials:'include'.
# - Proyecto backend usa DDD + Arquitectura Hexagonal (ports/adapters). YA EXISTE.
# - Restricción clave: NO reestructurar el proyecto ni desviarse del estilo actual.
#   Debes revisar lo ya implementado y SOLO extender si es estrictamente necesario.

# Objetivo principal:
# Asegurar que la API provea TODO lo que necesita el frontend (excepto el videojuego, que está embebido en el frontend).
# Para ello debes:
# 1) Identificar los endpoints necesarios (mínimos pero completos).
# 2) Especificar para cada endpoint el contrato completo:
#    - método + path
#    - responsabilidad
#    - JSON request (si aplica)
#    - JSON response (si aplica)
#    - códigos HTTP posibles (200/201/204/400/401/403/404/409)
# 3) Comparar ese contrato con los DTOs y endpoints existentes en la API (lo ya hecho).
# 4) Proponer SOLO las extensiones necesarias (nuevos DTOs/endpoints/mapeos) para cubrir brechas.
# 5) Mantener la coherencia con DDD + Hexagonal:
#    - Application services / use-cases
#    - input ports / output ports
#    - adapters (REST, persistence, etc.)
#    - NO exponer entidades de dominio directo en la API (usar DTOs).
# 6) Ser explícito sobre qué parte va en qué capa y por qué.

# Restricciones adicionales:
# - NO proponer endpoints o lógica del motor del videojuego (Unity/WebGL) porque está embebido en el frontend.
# - NO incluir código de frontend.
# - NO introducir refactors estructurales grandes: evitar “re-arquitecturar”.
# - Debes respetar naming conventions ya existentes en el proyecto.
# - Evitar “CRUD puro” si rompe el enfoque de casos de uso, pero no forzar CQRS si no existe.

# Manejo de errores de dominio (muy importante):
# - Para errores de dominio, la API debe devolver el mensaje de las excepciones de dominio ya creadas.
# - Si detectas que faltan excepciones de dominio, puedes proponer añadir nuevas (mínimas).
# - Debes definir un formato JSON de error consistente y mapear:
#   * DomainException -> 400/409 según corresponda
#   * NotFound -> 404
#   * Unauthorized -> 401
#   * Forbidden -> 403
# - Debe incluirse el campo "message" EXACTAMENTE igual al mensaje de la excepción de dominio.

# =========================================================
# CONTRATOS PROPUESTOS (JSON REQUEST/RESPONSE) - BASELINE
# (Debes usar estos como referencia para comparar con los DTOs actuales en la API.)
# =========================================================

# 1) Autenticación / sesión
# ---------------------------------------------------------
# GET /api/v1/auth/login?redirect=<url>
# - Inicia login OIDC (redirige a Spring Security /oauth2/authorization/keycloak).
# - Response: 302 Redirect (no JSON)
#
# GET /api/v1/auth/me
# - Verifica sesión y retorna identidad + roles (para gating del frontend).
# - 200 Response JSON:
# {
#   "username": "dmora@udla.edu.ec",
#   "email": "dmora@udla.edu.ec",
#   "fullName": "Chris Mora",
#   "roles": ["ROLE_STUDENT"]
# }
# - 401: sin sesión (sin body o vacío)
#
# POST /api/v1/auth/logout
# - Cierra sesión local y dispara logout OIDC (Keycloak), con post-logout bridge.
# - Response: 302 hacia Keycloak logout (no JSON) o 204 si se decide logout local-only.
#
# GET /api/v1/auth/post-logout
# - Bridge post logout: redirige al frontend (ej: http://localhost:3000/logged-out)
# - Response: 302 (no JSON)

# 2) Caso de uso: obtener ID del estudiante por correo (correo único)
# ---------------------------------------------------------
# NOTA: Idealmente el backend lo infiere de la sesión (OIDC email/preferred_username),
# pero se requiere explícitamente poder obtener id por correo.
#
# Opción recomendada (BFF-friendly): el endpoint resuelve el email desde la sesión
# y NO requiere que el frontend envíe email (evita spoofing).
#
# GET /api/v1/students/me
# - 200 Response JSON:
# {
#   "studentId": "3dbc6da4-e2b6-4712-8710-b6c7291abf65",
#   "email": "dmora@udla.edu.ec",
#   "fullName": "Chris Mora"
# }
# - 401/403 según sesión/rol
#
# (Si insistes en lookup explícito por email, debe ser ROLE_ADMIN)
# GET /api/v1/students/lookup?email=dmora@udla.edu.ec
# - 200 Response JSON (igual al anterior)
# - 403 si no ROLE_ADMIN

# 3) Caso de uso: obtener curso del estudiante por su ID
# ---------------------------------------------------------
# GET /api/v1/students/{studentId}/course
# - 200 Response JSON:
# {
#   "courseId": "ISWZ3104",
#   "courseName": "MARKETING I",
#   "term": "2026A",
#   "teacherName": "Nombre Docente (opcional)"
# }
# - 404 si studentId no existe o no tiene curso
# - 403 si studentId no corresponde a la sesión (salvo ROLE_ADMIN)

# 4) Caso de uso: obtener tareas del curso por ID de curso
# ---------------------------------------------------------
# GET /api/v1/courses/{courseId}/tasks
# - 200 Response JSON:
# [
#   {
#     "id": "1",
#     "title": "Análisis de Mercado: Eco-Friendly",
#     "description": "Identifica las oportunidades...",
#     "deadline": "2026-02-15",
#     "status": "PENDING",       # PENDING | COMPLETED | EXPIRED
#     "type": "ASSIGNMENT",      # ASSIGNMENT | EVALUATION
#     "attempts": 0,
#     "maxAttempts": 3,
#     "minScore": 0.7
#   }
# ]
# - 404 si courseId no existe
#
# (Alternativa BFF simplificada si quieres evitar pasar courseId desde frontend):
# GET /api/v1/tasks
# - Devuelve las tareas del curso del estudiante autenticado (internamente resuelve student->course->tasks)
# - Response: mismo array

# 5) Caso de uso: obtener intentos de una tarea por taskId
# ---------------------------------------------------------
# GET /api/v1/tasks/{taskId}/attempts
# - 200 Response JSON:
# [
#   {
#     "attemptId": "att-123",
#     "taskId": "2",
#     "startedAt": "2026-01-14T10:10:00Z",
#     "finishedAt": "2026-01-14T10:30:00Z",
#     "status": "FINISHED",      # IN_PROGRESS | FINISHED
#     "outcome": "WIN",          # WIN | LOSE (o APPROVED/DISAPPROVED si ya existe)
#     "score": 0.85
#   }
# ]
# - 404 si taskId no existe o no pertenece al curso del estudiante
# - 403 si no corresponde a la sesión (salvo ROLE_ADMIN)

# 6) Caso de uso: obtener métricas de desempeño por attemptId
# ---------------------------------------------------------
# GET /api/v1/attempts/{attemptId}/metrics
# - 200 Response JSON:
# {
#   "attemptId": "att-123",
#   "taskId": "2",
#   "profileDiscoveryPercentage": 0.85,  # o "score" si ya existe así en el dominio
#   "finalAcceptance": 0.82,             # opcional según tu modelo
#   "remainingBudget": 450,
#   "totalTurnsUsed": 5,
#   "finalOutcome": "WIN",
#   "evaluatedAt": "2026-01-14T10:30:00Z"
# }
# - 404 si attemptId no existe o no pertenece al estudiante

# 7) Formato estándar de error (para comparar/implementar)
# ---------------------------------------------------------
# Cuando ocurra un error (incluyendo DomainException), devolver:
# - Content-Type: application/json
# - JSON:
# {
#   "code": "DOMAIN_ERROR",          # o un código más específico si ya existe
#   "message": "MENSAJE EXACTO de la excepción de dominio",
#   "details": { }                  # opcional (campos, ids, validaciones), sin filtrar info sensible
# }
# Códigos:
# - 400: validaciones de dominio / request inválido
# - 409: conflicto de dominio (ej: estado inválido, duplicados)
# - 404: no encontrado
# - 401: no autenticado
# - 403: no autorizado

# =========================================================
# TU TAREA
# =========================================================
# 1) Revisa el código existente del proyecto (endpoints, controllers, DTOs, application services, ports/adapters).
# 2) Compara lo existente contra los contratos anteriores (request/response JSON).
# 3) Produce un informe con:
#    A) Tabla/listado de endpoints existentes y su contrato actual (según código).
#    B) Brechas: qué falta para cubrir cada caso de uso obligatorio.
#    C) Propuesta mínima de cambios (sin re-arquitecturar):
#       - nuevos endpoints (si hacen falta)
#       - DTOs request/response
#       - mapeos
#       - puertos/adaptadores necesarios
#       - excepciones de dominio faltantes (solo si necesarias)
#    D) Recomendaciones de seguridad y consistencia (sin sobre-optimizar).
#
# IMPORTANTE:
# - No inventes estructuras inexistentes: apóyate en lo que veas en el repo.
# - Si hay decisiones dudosas (por ejemplo, endpoint expone entity de dominio), sugiere corrección mínima.
# - Mantén el enfoque en habilitar el frontend con contratos claros y estables.
#
# Entregables esperados:
# - Lista final de endpoints que se usarán por el frontend (mínimo necesario).
# - Para cada endpoint, request/response JSON definitivos (los que deben quedar implementados).
# - Lista de DTOs (nombres sugeridos) y en qué paquete/capa van.
# - Lista de excepciones de dominio usadas/creadas y su mapping HTTP.
