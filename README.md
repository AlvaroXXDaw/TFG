# Oasis Club Proyecto

Aplicacion web para la gestion de un club deportivo.

## Stack

- Frontend: Angular
- Backend: Spring Boot (Java 21)
- Base de datos: PostgreSQL
- Despliegue: Render (backend) + Vercel (frontend)

## Entornos en Producción

- **Frontend (Web App):** [https://tfg-mocha.vercel.app](https://tfg-mocha.vercel.app)
- **Backend (API Base):** [https://oasisclub-backend.onrender.com](https://oasisclub-backend.onrender.com)
- **Documentación Swagger:** [https://oasisclub-backend.onrender.com/swagger-ui/index.html](https://oasisclub-backend.onrender.com/swagger-ui/index.html)
## Estructura del repositorio

```text
Oasis Club/
|-- frontend/
|-- backend/
|-- docs/
|-- docker-compose.yml
|-- .env.example
`-- README.md
```

## Funcionalidades principales

- Autenticacion con JWT (login, registro, recuperacion de clave).
- Gestion de clientes (admin).
- Gestion de reservas y bloques de mantenimiento.
- Consulta de disponibilidad por pista y fecha.
- Gestion de pistas (admin).
- Perfil de usuario con resumen de actividad.
- Rutinas de gimnasio por cliente.

## Roles

- `ADMIN`: gestiona clientes, pistas y reservas.
- `MEMBER`: gestiona su actividad y reservas.

## Variables de entorno

Copiar `.env.example` a `.env` y completar valores.

## Ejecucion local (modo desarrollo)

1. Levantar Postgres:

```powershell
docker compose up -d postgres
```

2. Ejecutar backend:

```powershell
cd backend
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

3. Ejecutar frontend:

```powershell
cd frontend
npm install
npm run dev
```

`npm run dev` usa el backend local por proxy (`http://localhost:8080`). Para probar el frontend local contra el backend desplegado en Render:

```powershell
cd frontend
npm run dev:render
```

En Render, el backend debe tener `JWT_SECRET` configurado con un valor no vacio de al menos 32 caracteres. Si esta vacio, el login falla y las pantallas admin no cargan pistas ni horarios.

## Build y test rapido

Backend:

```powershell
cd backend
.\mvnw.cmd test
```

Frontend:

```powershell
cd frontend
npm run build
npm run test -- --watch=false
```

