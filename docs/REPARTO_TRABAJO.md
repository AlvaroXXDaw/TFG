# Reparto de Trabajo

## Equipo

- Desarrollador A: Frontend
- Desarrollador B: Backend

## Frontend (A)

- Arquitectura Angular y rutas.
- Componentes de:
  - login/registro/recuperacion de clave,
  - reservas,
  - perfil,
  - admin (clientes, pistas, reservas).
- Guardas de acceso y estado de autenticacion.
- Integracion con API mediante servicios e interceptores.
- Ajustes de estilos y experiencia de usuario.

## Backend (B)

- Arquitectura Spring Boot y seguridad JWT.
- Modulos de negocio:
  - auth,
  - clients,
  - courts,
  - reservations,
  - profile,
  - gym.
- Persistencia con JPA y migraciones Flyway.
- Manejo global de errores y validaciones.
- Tests unitarios de servicios.

## Trabajo compartido

- Definicion de contrato API (DTOs y respuestas).
- Pruebas integradas frontend-backend.
- Ajustes de permisos y reglas de negocio.
- Preparacion de despliegue en Render/Vercel.
