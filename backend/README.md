# Backend 

API REST en Spring Boot para la gestion del club deportivo.

## Requisitos

- Java 21
- Maven Wrapper (incluido)
- PostgreSQL

## Configuracion

Variables importantes para render:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `CORS_ORIGINS`
- `FRONTEND_URL`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`
- `MAIL_OVERRIDE_TO`

## Arranque en local

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

## Endpoints base

Base path: `/api/v1`

- Auth: `/auth/*`
- Clients: `/clients/*`
- Courts: `/courts/*`
- Reservations: `/reservations/*`
- Availability: `/availability`
- Profile: `/profile/{clientId}`
- Gym: `/gym/routines/{clientId}`

## Swagger

- UI: `/swagger-ui.html`
- OpenAPI: `/v3/api-docs`

## Tests

Ejecutar todos:

```powershell
.\mvnw.cmd test
```

Ejecutar solo algunos tests:

```powershell
.\mvnw.cmd "-Dtest=AuthServiceTest,ClientServiceTest,ReservationServiceTest" test
```
