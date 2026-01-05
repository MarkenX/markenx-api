# Sistema de Configuración para el Juego de Simulación de Marketing

## Introducción

Este documento describe el sistema de configuración para un juego de simulación de marketing basado en un modelo
dimensional flexible. El sistema permite crear escenarios de juego completamente configurables sin necesidad de código
rígido.

### Características principales

- **Modelo dimensional normalizado**: Valores entre 0.0 y 1.0
- **Escalable y reutilizable**: Simula infinitos escenarios
- **Matemáticamente transparente**: Cálculos basados en distancias ponderadas
- **Configuración centralizada**: Todo el escenario en un único archivo JSON

---

## Teoría del Sistema

El juego sustituye etiquetas rígidas (como "consumidor premium" o "producto económico") por un **sistema flexible de
dimensiones numéricas normalizadas** con valores entre 0.0 y 1.0.

### Concepto Fundamental

Cada aspecto del producto y del consumidor se representa mediante **dimensiones medibles** que influyen en la decisión
de compra. La compatibilidad entre producto y consumidor se calcula mediante métricas de distancia matemática.

---

## Principios Clave

### 1. Dimensión

**Definición**: Atributo medible que influye en la decisión de compra.

**Ejemplos**:

- Sensibilidad al precio
- Interés ecológico
- Exigencia de calidad
- Reconocimiento social
- Facilidad de uso

### 2. ConsumerExpectation (Expectativa del Consumidor)

**Definición**: Representa lo que el consumidor desea o valora en cada dimensión.

**Rango**: 0.0 - 1.0

- **0.0**: No le importa en absoluto
- **1.0**: Es extremadamente importante

### 3. ProductInitialOffer (Oferta Inicial del Producto)

**Definición**: Representa lo que el producto ofrece inicialmente en cada dimensión.

**Rango**: 0.0 - 1.0

- **0.0**: No ofrece nada en esta dimensión
- **1.0**: Ofrece el máximo posible

### 4. Matching (Compatibilidad)

**Cálculo**: Se utiliza una métrica de distancia Manhattan ponderada entre:

- Los valores de `consumerExpectation`
- Los valores actuales del producto (`productInitialOffer` + deltas acumulados de acciones)

**Objetivo**: Minimizar la distancia entre lo que el consumidor espera y lo que el producto ofrece.

### 5. Objetivo del Jugador

Aplicar **acciones de marketing** que modifiquen el perfil del producto para alcanzar o superar el
`targetAcceptanceScore` del consumidor (ejemplo: 80%).

### 6. Acciones

**Definición**: Representan decisiones reales de marketing según las 4P.

**Características**:

- Tienen un **costo** económico
- Pertenecen a una **categoría** (Producto, Precio, Plaza, Promoción)
- Generan **efectos** (deltas) sobre dimensiones específicas
- Pueden tener **prerequisitos** (dependencias de otras acciones)

### 7. Eventos

**Definición**: Factores externos que alteran temporalmente la importancia de ciertas dimensiones.

**Mecanismo**: Aplican multiplicadores a las dimensiones del consumidor, simulando cambios en el mercado o tendencias
sociales.

---

## Estructura del JSON de Configuración

El archivo de configuración define completamente un escenario de juego mediante tres secciones principales.

### Vista General

```
{
  "dimensions": [
    "..."
  ],
  "consumer": {
    "..."
  },
  "actions": [
    "..."
  ],
  "events": [
    "..."
  ]
}
```

---

## Referencia de Elementos

### 1. Dimensions

**Descripción**: Lista de todas las dimensiones del escenario.

**Estructura**:

```json
{
  "id": "UUID",
  "name": "Nombre de la dimensión",
  "description": "Explicación detallada",
  "consumerExpectation": "Decimal entre 0.0 - 1.0",
  "productInitialOffer": "Decimal entre 0.0 - 1.0"
}
```

**Campos**:

| Campo                 | Tipo            | Descripción                                |
|-----------------------|-----------------|--------------------------------------------|
| `id`                  | UUID            | Identificador único de la dimensión        |
| `name`                | String          | Nombre de la dimensión                     |
| `description`         | String          | Explicación detallada de la dimensión      |
| `consumerExpectation` | Float (0.0-1.0) | Cuánto valora el consumidor esta dimensión |
| `productInitialOffer` | Float (0.0-1.0) | Cuánto ofrece el producto inicialmente     |

**Ejemplo**:

```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "name": "Sensibilidad al precio",
  "description": "Cuánto le importa al consumidor ahorrar dinero vs. pagar más por otras cualidades",
  "consumerExpectation": 0.40,
  "productInitialOffer": 0.50
}
```

### 2. Consumer

**Descripción**: Información del consumidor objetivo del escenario.

**Estructura**:

```json
{
  "name": "Nombre del consumidor",
  "age": 30,
  "budget": 1000,
  "targetAcceptanceScore": "Decimal entre 0.0 - 1.0"
}
```

**Campos**:

| Campo                   | Tipo               | Descripción                                              |
|-------------------------|--------------------|----------------------------------------------------------|
| `name`                  | String             | Nombre visible del consumidor                            |
| `age`                   | Integer (opcional) | Edad del consumidor                                      |
| `budget`                | Integer (opcional) | Presupuesto aproximado                                   |
| `targetAcceptanceScore` | Float (0.0-1.0)    | Porcentaje mínimo de compatibilidad requerido para ganar |

**Ejemplo**:

```json
{
  "name": "Barry Seal",
  "age": 30,
  "budget": 1300,
  "targetAcceptanceScore": 0.80
}
```

### 3. Actions

**Descripción**: Lista de acciones disponibles para el jugador.

**Estructura**:

```json
{
  "id": "UUID",
  "name": "Nombre de la acción",
  "description": "Descripción detallada",
  "cost": 100,
  "category": "PRODUCTION | DESIGN | PRICE | PLACEMENT | PROMOTION | RESEARCH",
  "prerequisiteActionId": "UUID o null",
  "effects": [
    {
      "dimensionId": "UUID",
      "delta": "Decimal entre -1.0 - 1.0"
    }
  ]
}
```

**Campos**:

| Campo                  | Tipo        | Descripción                           |
|------------------------|-------------|---------------------------------------|
| `id`                   | UUID        | Identificador único de la acción      |
| `name`                 | String      | Nombre visible de la acción           |
| `description`          | String      | Descripción detallada del efecto      |
| `cost`                 | Integer     | Costo en presupuesto del jugador      |
| `category`             | Enum        | Categoría de las 4P + Research/Design |
| `prerequisiteActionId` | UUID o null | ID de acción requerida previamente    |
| `effects`              | Array       | Lista de efectos sobre dimensiones    |

**Categorías válidas**:

- `PRODUCTION`: Cambios en el producto físico
- `DESIGN`: Cambios en diseño y presentación
- `PRICE`: Estrategias de precio
- `PLACEMENT`: Canales de distribución
- `PROMOTION`: Comunicación y publicidad
- `RESEARCH`: Investigación de mercado

**Efecto (Effect)**:

| Campo         | Tipo               | Descripción                              |
|---------------|--------------------|------------------------------------------|
| `dimensionId` | UUID               | ID de la dimensión afectada              |
| `delta`       | Float (-1.0 a 1.0) | Cambio aplicado a la oferta del producto |

**Ejemplo**:

```json
{
  "id": "f1a2b3c4-d5e6-7890-abcd-ef1234567890",
  "name": "Empaque Reciclado",
  "description": "Cartón 100% reciclado.",
  "cost": 150,
  "category": "PRODUCTION",
  "isInitiallyLocked": false,
  "prerequisiteActionId": null,
  "effects": [
    {
      "dimensionId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "delta": 0.15
    }
  ]
}
```

### 4. Events

**Descripción**: Eventos externos que modifican temporalmente la importancia de dimensiones.

**Estructura**:

```json
{
  "id": "UUID",
  "title": "Título del evento",
  "description": "Descripción del evento",
  "effects": [
    {
      "dimensionId": "UUID",
      "weightMultiplier": "Decimal entre 0.0 - 1.0"
    }
  ]
}
```

**Campos**:

| Campo         | Tipo   | Descripción                          |
|---------------|--------|--------------------------------------|
| `id`          | UUID   | Identificador único del evento       |
| `title`       | String | Título visible del evento            |
| `description` | String | Descripción del evento y su contexto |
| `effects`     | Array  | Lista de efectos sobre dimensiones   |

**Efecto de Evento**:

| Campo              | Tipo         | Descripción                           |
|--------------------|--------------|---------------------------------------|
| `dimensionId`      | UUID         | ID de la dimensión afectada           |
| `weightMultiplier` | Float (≥1.0) | Multiplicador de importancia temporal |

**Ejemplo**:

```json
{
  "id": "h3i4j5k6-l7m8-9012-nopq-345678901234",
  "title": "El Mundo Se Vuelve Más Verde",
  "description": "Impulso global por la sostenibilidad gana fuerza.",
  "effects": [
    {
      "dimensionId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "weightMultiplier": 5.0
    }
  ]
}
```

---

## Ejemplo Completo

### Escenario: Consumidor Ecológico

Este ejemplo representa un consumidor que valora altamente la sostenibilidad, pero el producto inicial tiene baja oferta
ecológica. El jugador debe invertir estratégicamente para alcanzar el 80% de aceptación.

```json
{
  "dimensions": [
    {
      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "name": "PriceSensitivity",
      "displayName": "Sensibilidad al precio",
      "description": "Cuánto le importa al consumidor ahorrar dinero vs. pagar más por otras cualidades",
      "consumerExpectation": 0.40,
      "productInitialOffer": 0.50
    },
    {
      "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "name": "EcoInterest",
      "displayName": "Interés ecológico",
      "description": "Preocupación por la sostenibilidad y el impacto ambiental",
      "consumerExpectation": 0.95,
      "productInitialOffer": 0.10
    },
    {
      "id": "c3d4e5f6-a7b8-9012-cdef-123456789012",
      "name": "QualityExpectation",
      "displayName": "Exigencia de calidad",
      "description": "Nivel de calidad y durabilidad esperado",
      "consumerExpectation": 0.80,
      "productInitialOffer": 0.40
    },
    {
      "id": "d4e5f6a7-b8c9-0123-def1-234567890123",
      "name": "SocialRecognition",
      "displayName": "Reconocimiento social",
      "description": "Importancia del estatus y la imagen que proyecta el producto",
      "consumerExpectation": 0.60,
      "productInitialOffer": 0.30
    },
    {
      "id": "e5f6a7b8-c9d0-1234-ef12-345678901234",
      "name": "EaseOfUse",
      "displayName": "Facilidad de uso",
      "description": "Preferencia por productos intuitivos y simples",
      "consumerExpectation": 0.70,
      "productInitialOffer": 0.50
    }
  ],
  "consumer": {
    "name": "Barry Seal",
    "age": 30,
    "budget": 1300,
    "targetAcceptanceScore": 0.80
  },
  "actions": [
    {
      "id": "f1a2b3c4-d5e6-7890-abcd-ef1234567890",
      "name": "Empaque Reciclado",
      "description": "Cartón 100% reciclado.",
      "cost": 150,
      "category": "PRODUCTION",
      "isInitiallyLocked": false,
      "prerequisiteActionId": null,
      "effects": [
        {
          "dimensionId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
          "delta": 0.15
        }
      ]
    },
    {
      "id": "g2h3i4j5-k6l7-8901-mnop-123456789012",
      "name": "Material Biodegradable",
      "description": "Se degrada en 30 días.",
      "cost": 200,
      "category": "PRODUCTION",
      "isInitiallyLocked": true,
      "prerequisiteActionId": "f1a2b3c4-d5e6-7890-abcd-ef1234567890",
      "effects": [
        {
          "dimensionId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
          "delta": 0.25
        }
      ]
    },
    {
      "id": "i3j4k5l6-m7n8-9012-opqr-345678901234",
      "name": "Descuento por Reciclaje",
      "description": "5% de descuento si el cliente trae envase antiguo.",
      "cost": 80,
      "category": "PRICE",
      "isInitiallyLocked": false,
      "prerequisiteActionId": null,
      "effects": [
        {
          "dimensionId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
          "delta": -0.10
        },
        {
          "dimensionId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
          "delta": 0.10
        }
      ]
    }
  ],
  "events": [
    {
      "id": "h3i4j5k6-l7m8-9012-nopq-345678901234",
      "title": "El Mundo Se Vuelve Más Verde",
      "description": "Impulso global por la sostenibilidad gana fuerza.",
      "effects": [
        {
          "dimensionId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
          "weightMultiplier": 5.0
        }
      ]
    }
  ]
}
```

### Análisis del Escenario

**Situación inicial**:

- El consumidor valora el interés ecológico en 0.95 (muy alto)
- El producto ofrece solo 0.10 (muy bajo)
- Brecha inicial: 0.85 puntos

**Estrategia ganadora**:

1. Aplicar "Empaque Reciclado" (+0.15 eco, -150 presupuesto)
2. Desbloquear y aplicar "Material Biodegradable" (+0.25 eco, -200 presupuesto)
3. Aplicar "Descuento por Reciclaje" (+0.10 eco, -0.10 precio, -80 presupuesto)

**Resultado**:

- Oferta ecológica final: 0.10 + 0.15 + 0.25 + 0.10 = 0.60
- Presupuesto usado: 430 de 1300
- Compatibilidad alcanzada: ≥ 80%

---

## Glosario

| Término                     | Definición                                              |
|-----------------------------|---------------------------------------------------------|
| **Dimensión**               | Atributo medible del producto/consumidor (0.0-1.0)      |
| **Delta**                   | Cambio aplicado a una dimensión por una acción          |
| **Compatibilidad**          | Métrica de ajuste entre producto y consumidor (0.0-1.0) |
| **Target Acceptance Score** | Umbral mínimo de compatibilidad para ganar              |
| **Prerequisito**            | Acción que debe ejecutarse antes de desbloquear otra    |
| **Weight Multiplier**       | Factor de amplificación temporal de importancia         |
| **4P**                      | Producto, Precio, Plaza (distribución), Promoción       |
