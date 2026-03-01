# API Gestión de Pólizas (Prueba Técnica) – Módulo 2

API REST en **Spring Boot** para gestión básica de **pólizas** y **riesgos**, con:
- **Seguridad mínima por header obligatorio**
- **Persistencia en memoria (sin BD)**: al reiniciar la app se restauran **datos semilla**
- **Mock externo obligatorio CORE** para simular notificación (y ver logs)

---

## Stack / Enfoque
- Java + Spring Boot
- Arquitectura por capas: controller / service / repository
- Integración a CORE vía adapter (CoreClient + RestTemplate)

---

## Seguridad mínima (OBLIGATORIA)
Todas las peticiones deben incluir el header:

- `x-api-key: 123456`

Si falta o es incorrecto, la API responde:
- `401 Unauthorized`

Sugerencia Postman:
- Usa variable `{{apiKey}}` y define el header a nivel de colección.

---

## Cómo ejecutar

### Opción A: IntelliJ
1. Abrir el proyecto.
2. Ejecutar `PolizasApiApplication`.
3. API disponible en `http://localhost:8080`

### Opción B: Terminal (Maven)
En la carpeta del proyecto:
```bash
mvn spring-boot:run