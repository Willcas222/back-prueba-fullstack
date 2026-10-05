# Mesa de ayuda

Backend REST para administrar solicitudes de soporte. El frontend React se mantiene en la carpeta hermana `../front-fullstack`, separado del repositorio del backend.

## Requisitos

- Java 17 o superior
- PostgreSQL 14 o superior
- Node.js 20.19+ y npm (solo para el frontend)

## Base de datos

Crea una base PostgreSQL antes de iniciar el backend:

```sql
CREATE DATABASE tickets;
```

Configura las variables de conexión usando `.env.example` como referencia. Spring Boot lee las variables del entorno del proceso; el archivo `.env.example` no se carga automáticamente. En PowerShell, por ejemplo:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/tickets"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "tu-clave"
```

Hibernate actualiza el esquema existente con `JPA_DDL_AUTO=update`. Para producción se recomienda gestionar los cambios de esquema con migraciones versionadas y cambiar esta opción a `validate`.

## Ejecutar backend

Desde la carpeta `prueba-fullstack`:

```powershell
.\mvnw.cmd spring-boot:run
```

El servicio queda disponible en `http://localhost:8080`. Si el frontend se sirve desde otro origen, configura `FRONTEND_ORIGIN`. Ejecuta las pruebas y el build con:

```powershell
.\mvnw.cmd test
```

## Ejecutar frontend

Desde la carpeta separada `front-fullstack` (consulta su `.env.example` y copia ese archivo como `.env.local` si necesitas cambiar la URL de la API):

```powershell
npm install
npm run dev
```

Vite publica la aplicación en `http://localhost:5173`. Si el backend está en otra dirección, configura `VITE_API_URL` en `front-fullstack/.env.local` (por ejemplo, `VITE_API_URL=http://localhost:8080/api`). La API permite CORS para ese origen; ajusta `frontend.origin` si utilizas otro.

## API

Todos los valores enumerados viajan en mayúsculas con guion bajo para conservar el formato de los enum Java.

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/tickets` | Crear solicitud |
| `GET` | `/api/tickets/{id}` | Consultar detalle |
| `GET` | `/api/tickets` | Buscar con `status`, `priority`, `category`, `page`, `size`, `sortBy` y `sortDirection` |
| `PUT` | `/api/tickets/{id}` | Editar solicitud pendiente |
| `PATCH` | `/api/tickets/{id}/status` | Cambiar estado con responsable y observación opcional |
| `POST` | `/api/tickets/{id}/cancel` | Cancelar solicitud pendiente |
| `GET` | `/api/tickets/{id}/history` | Consultar historial de cambios |

La paginación usa índice de página desde cero y permite tamaños entre 1 y 100. Los campos permitidos para ordenar son `id`, `title`, `requestUser`, `category`, `priority`, `status`, `createdAt` y `updatedAt`. Una observación es obligatoria al resolver una solicitud crítica.

## Notificaciones SMS

Las notificaciones están deshabilitadas de forma predeterminada. Para activarlas configura `HABLAME_ENABLED=true`, las credenciales Hablame y `HABLAME_URL` con la ruta asignada a tu cuenta, además de un teléfono internacional en la solicitud. Las credenciales se leen desde el entorno y no deben guardarse en el repositorio.

## Arquitectura Hexagonal

El dominio (`domain`) contiene entidades y reglas de negocio independientes de Spring y la base de datos. Los casos de uso (`application`) dependen de puertos; los adaptadores de entrada exponen REST, y los adaptadores de salida implementan la persistencia PostgreSQL y la notificación Hablame. Esta inversión de dependencias permite probar las reglas sin infraestructura y reemplazar proveedores sin modificar el dominio.

## Ramas

El repositorio utiliza `main` para la integración estable y `develop` para el desarrollo.
