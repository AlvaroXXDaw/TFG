# Memoria Tecnica

## 1. Descripcion general

Oasis Club es una aplicacion web para gestionar un club deportivo, con foco en:

- reservas de pistas,
- administracion de clientes,
- planificacion de rutinas de gimnasio,
- y perfil de usuario.

## 2. Arquitectura

### Frontend

- Angular con componentes standalone.
- Guardas de ruta para `guest`, `member` y `admin`.
- Servicios HTTP para cada modulo (`auth`, `clients`, `courts`, `reservations`, `profile`, `gym`).
- Interceptores para JWT y gestion de errores API.

### Backend

- Spring Boot con capas:
  - `controller`
  - `service`
  - `repository`
  - `mapper`
  - `dto`
  - `entity`
- Seguridad con JWT en `SecurityConfig` + `JwtFilter`.
- Validacion con Jakarta Validation.
- Errores centralizados en `GlobalExceptionHandler`.

### Persistencia

- PostgreSQL.
- Migraciones con Flyway (`V1..V5`).

## 3. Modulos funcionales

- `auth`: login, registro, forgot/reset password.
- `clients`: alta, listado, borrado, cambio de plan (admin).
- `courts`: listado, alta y borrado de pistas.
- `reservations`: listar, crear, borrar, mantenimiento, disponibilidad.
- `profile`: resumen del cliente y sus reservas.
- `gym`: rutina por cliente (lectura y actualizacion).

## 4. Reglas de negocio importantes

- Solo `ADMIN` puede gestionar clientes y pistas.
- `MEMBER` solo puede operar sobre sus datos (perfil/rutina/reservas).
- El plan `BASIC` limita reservas activas por dia (validado en controlador de reservas).
- No se permiten reservas en slots ya ocupados.

## 5. Calidad y pruebas

- Tests unitarios backend con JUnit 5 y Mockito.
- Build frontend validado.
- Smoke test real ejecutado contra Render para endpoints principales.

## 6. Despliegue

- Backend en Render.
- Frontend en Vercel.
- Variables sensibles por entorno (no versionadas).
