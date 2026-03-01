# Módulo 1 – Diseño de Arquitectura (Prueba Técnica)

## 1. Arquitectura de Alto Nivel
La solución se implementa como una API REST en Spring Boot con arquitectura por capas y componentes separados:

- **API Gateway (conceptual):** entrada única (en un entorno real). En esta prueba se asume el cliente llamando directo al servicio.
- **Servicio Pólizas:** expone endpoints y reglas de negocio de pólizas (crear, cancelar, renovar, consultar).
- **Servicio Riesgos:** expone endpoints y reglas de negocio de riesgos (crear/listar/cancelar) y validaciones (solo COLECTIVA).
- **Servicio Notificaciones:** responsabilidad de emitir eventos de dominio hacia CORE (en la prueba se simula llamando un mock).
- **Adapter CORE:** integración desacoplada (puerto/interfaz + implementación RestTemplate).
- **BD:** en esta prueba es **in-memory** (listas). Se deja preparado el diseño para migrar a H2/JPA.

## 2. Patrones de Diseño (3) y Justificación

### 2.1. Arquitectura por Capas (Layered Architecture)
Separa responsabilidades (API/negocio/datos), facilita mantenimiento, pruebas y evolución.

### 2.2. Ports & Adapters (Hexagonal) para integración con CORE
El CORE se trata como una dependencia externa. Se define un **puerto** (interface `CoreClient`) y un **adapter** (`CoreClientImpl`).
Permite cambiar la implementación (mock vs integración real) sin tocar reglas de negocio.

### 2.3. Event-driven (Notificación)
Operaciones como **CREACIÓN, CANCELACIÓN, RENOVACIÓN, CAMBIO DE RIESGO** generan un “evento” que se envía al CORE (mock).
Esto desacopla la operación principal del mecanismo de notificación y habilita evolución a colas/reintentos.

## 3. Modelo de Datos Principal (Entidades)
- **Poliza**
  - numeroPoliza (String)
  - titular (String)
  - tipo (INDIVIDUAL | COLECTIVA)
  - estado (ACTIVA | CANCELADA | RENOVADA)
  - canon (double)
  - prima (double)

- **Riesgo**
  - id (String)
  - polizaId (String)
  - descripcion (String)
  - estado (ACTIVO | CANCELADO)

## 4. Diagrama (Mermaid) – Componentes solicitados

```mermaid
flowchart LR
  C[Cliente / Postman] --> G[API Gateway (conceptual)]
  G --> P[Servicio Pólizas (Controllers/Service)]
  G --> R[Servicio Riesgos (Controllers/Service)]

  P --> RP[Repositorio Pólizas (in-memory / JPA futuro)]
  R --> RR[Repositorio Riesgos (in-memory / JPA futuro)]

  P --> N[Servicio Notificaciones]
  R --> N

  N --> A[Adapter CORE (CoreClient)]
  A --> CM[CORE Mock (/core-mock/evento)]

  RP --> DB[(BD / H2 futuro)]
  RR --> DB