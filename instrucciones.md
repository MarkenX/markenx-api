Actúa como Senior Software Architect + Backend Engineer experto en Spring Boot (Java 21, Maven), DDD, Arquitectura Hexagonal, Spring Modulith, Saga Pattern, JDBC (Commands), jOOQ (Queries), MySQL y Flyway.

Tienes acceso completo al repositorio y a la carpeta docs/.

FASE 1 – Aprendizaje obligatorio (no escribir código aún)

Analiza completamente la carpeta docs/, en especial docs/videogame-config.md. Identifica la estructura del JSON de configuración del videojuego, qué partes pertenecen a cada módulo, y las reglas de negocio explícitas e implícitas. El JSON no es una tabla, es un aggregate distribuido.

Analiza la arquitectura existente del monolito. Confirma el uso de DDD, Arquitectura Hexagonal y Spring Modulith. Identifica el uso de @ApplicationModule y @NamedInterface. Verifica la separación de capas domain, application e infrastructure. Confirma el uso de JDBC para commands, jOOQ para queries, MySQL como base de datos y Flyway para migraciones.

Estudia en detalle los módulos Student y User. Analiza cómo implementan el Saga Pattern, rollback o compensaciones, Domain Events, ports.incoming y ports.outgoing, así como el patrón GET/POST, los Application Services y el uso de excepciones de dominio. Estos módulos son el patrón obligatorio a seguir.

FASE 2 – Dominio: Configuración del Videojuego

Se deben implementar cinco módulos usando Spring Modulith. Los nombres de los módulos ya existen y no deben cambiarse. Cada módulo tiene su propio dominio, sus propias tablas y sigue el patrón GET/POST usado en los demás módulos del proyecto.

El módulo Escenario es el orquestador. Se identifica por una UUID. No contiene lógica de los otros módulos. Orquesta la comunicación mediante Domain Events y/o ports.incoming, siguiendo el enfoque más limpio utilizado en Student y User. Escenario es responsable de construir el JSON agregado completo, exponer un POST para guardar la configuración y un GET para obtenerla por UUID, y coordinar el guardado y la obtención distribuida.

FASE 3 – Diseño de los módulos

Para cada uno de los cinco módulos define explícitamente el Aggregate Root, entities, value objects, invariantes y excepciones de dominio. No se permiten anemic models ni lógica en adapters.

Define la capa de aplicación con commands para POST, queries para GET, application services y límites transaccionales claros.

Define ports.incoming y ports.outgoing. Los ports.outgoing deben cubrir persistencia y eventos si aplica.

Define la infraestructura con REST adapters, JDBC adapters para commands, jOOQ adapters para queries y migraciones Flyway.

FASE 4 – Consistencia, Saga y rollback

El guardado del Escenario debe ser atómico. Si un módulo falla, todo el proceso debe abortarse sin dejar datos inconsistentes. Reutiliza el patrón Saga existente en los módulos Student y User. Usa Domain Events y compensaciones explícitas o application services transaccionales según el estándar del proyecto. No introduzcas frameworks ni patrones nuevos.

FASE 5 – JSON como aggregate distribuido

El JSON de configuración es grande y está documentado en docs/videogame-config.md. No se persiste como JSON. Para POST, el JSON se descompone en commands por módulo. Para GET, el JSON se compone a partir de queries de cada módulo.

FASE 6 – Entregables

Explica el diseño, la responsabilidad de cada módulo, las relaciones entre módulos, el flujo completo del POST, el flujo completo del GET y la estrategia de rollback.

Genera el código con la estructura de paquetes por módulo, las clases principales completas, ejemplos de commands, events, ports y application services, y ejemplos de JSON de entrada y salida.

Justifica cómo la solución cumple DDD, Arquitectura Hexagonal y Spring Modulith, y cómo evita acoplamientos, inconsistencias y dependencias cíclicas.

Reglas finales

No colocar lógica en controllers.
No permitir acceso directo entre módulos.
No usar JPA.
No compartir tablas entre módulos.
Usar excepciones de dominio.
Usar lenguaje ubicuo.
Respetar estrictamente las convenciones del repositorio.

Antes de diseñar o codificar, explica cómo entiendes el dominio, el JSON del escenario y el rol de cada módulo.
