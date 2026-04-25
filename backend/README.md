# Backend 

API REST en Spring Boot para la gestion del club deportivo.

## Requisitos

- Java 21
- Maven Wrapper (incluido)
- PostgreSQL

## Configuracion

Variables importantes para Render:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`: obligatorio, no puede estar vacio. Debe tener al menos 32 caracteres para firmar los JWT.
- `CORS_ORIGINS`: `https://tfg-mocha.vercel.app`
- `FRONTEND_URL`: `https://tfg-mocha.vercel.app`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`
- `MAIL_OVERRIDE_TO`

Si `JWT_SECRET` esta vacio en Render, el login devuelve error 500 y el frontend no puede cargar datos protegidos como pistas u horarios.

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
