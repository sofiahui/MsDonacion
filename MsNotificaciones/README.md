# MsNotificaciones

Microservicio de notificaciones que consume eventos Kafka generados por **MsDonacion** y los expone via REST para que el frontend pueda consultarlos mediante polling.

## Arquitectura

```
MsDonacion --[Kafka: donacion-creada]--> MsNotificaciones --[GET /notificaciones]--> React Frontend
```

## Requisitos

- Java 21
- Maven 3.x
- Kafka corriendo en `localhost:9092`

## Ejecución

```bash
# Levantar Kafka (requiere Docker)
docker run -p 9092:9092 apache/kafka:3.7.0

# Ejecutar el microservicio
mvn spring-boot:run
```

El servicio quedará disponible en `http://localhost:8082`.

## Endpoints

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/notificaciones` | Retorna lista de alertas de donaciones recibidas |

**Respuesta ejemplo:**
```json
[
  "Nueva donación: 10 unidades de ropa en Centro Santiago",
  "Nueva donación: 5 unidades de alimento en Centro Norte"
]
```

## Tests

```bash
mvn test
```

Los tests de `NotificacionService` son unitarios puros (no requieren Kafka).

## Patrones implementados

- **Observer / Event-Driven**: `DonacionEventConsumer` reacciona a eventos Kafka sin acoplamiento con MsDonacion.
- **Service Layer**: `NotificacionService` encapsula la lógica de almacenamiento y consulta de notificaciones.
