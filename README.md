# Mesa de ayuda

Backend REST para administrar solicitudes de soporte. El frontend React se mantiene en la carpeta hermana `../front-fullstack`, separado del repositorio del backend.

## Ejecutar todo con Docker

Requiere Docker Desktop con Docker Compose. Desde `prueba-fullstack`:

```powershell
Copy-Item .env.example .env
docker compose up --build -d
```

Este repositorio contiene solo el backend: Compose levanta PostgreSQL 16 y Spring Boot. El frontend React se mantiene por separado en `front-fullstack`. PostgreSQL conserva los datos en el volumen `postgres_data`; el backend espera a que la base esté saludable. El esquema de tablas se crea/actualiza con Hibernate (`JPA_DDL_AUTO=update`).

- API REST: `http://localhost:8080/api/tickets`
- PostgreSQL desde el host: `localhost:5432` (base `tickets`)
- Frontend de desarrollo (si tienes `front-fullstack`): `http://localhost:5173`

Edita `.env` para personalizar `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `DB_PORT`, `SERVER_PORT` y `FRONTEND_ORIGIN` antes de iniciar los contenedores. `.env` es local y está excluido de Git. Cambiar las credenciales después de crear el volumen no modifica las credenciales que PostgreSQL ya guardó; para una base nueva puede borrarse el volumen con `docker compose down -v` (esto elimina todos sus datos).

Comandos útiles:

```powershell
docker compose ps
docker compose logs -f postgres backend
docker compose down
```

Para parar los servicios y preservar los datos utiliza `docker compose down`, sin `-v`. Usa credenciales fuertes y `JPA_DDL_AUTO=validate` junto con migraciones versionadas fuera del entorno local.

## Ejecutar sin Docker

Se requiere Java 17 o superior, PostgreSQL 14 o superior y Node.js 20.19+ con npm. Crea una base llamada `tickets`, configura `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` en el entorno, y luego ejecuta:

## Ejecutar backend

Desde la carpeta `prueba-fullstack`:

```powershell
.\mvnw.cmd spring-boot:run
```

El servicio queda disponible en `http://localhost:8080`. Si el frontend se sirve desde otro origen, configura `FRONTEND_ORIGIN`. Ejecuta las pruebas con:

```powershell
.\mvnw.cmd test
```

Para ejecutar el frontend de desarrollo sin Docker:

Desde la carpeta separada `front-fullstack` (consulta su `.env.example` y copia ese archivo como `.env.local` si necesitas cambiar la URL de la API):

```powershell
npm install
npm run dev
```

Vite publica la aplicación en `http://localhost:5173`. Si el backend está en otra dirección, configura `VITE_API_URL` en `front-fullstack/.env.local` (por ejemplo, `VITE_API_URL=http://localhost:8080/api`). La API permite CORS para ese origen; ajusta `FRONTEND_ORIGIN` si utilizas otro.

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

El backend Java utiliza el paquete raíz `com.tuempresa.soporte` y agrupa la funcionalidad de solicitudes de soporte por capa:

```text
com/tuempresa/soporte/
├── domain/
│   ├── model/          # Solicitud, HistorialCambios, Categoria, Prioridad, Estado
│   ├── exception/      # Excepciones del dominio
│   └── port/
│       ├── in/         # Interfaces de casos de uso y sus comandos
│       └── out/        # Contratos de persistencia y SMS
├── application/
│   └── service/        # Orquestación de casos de uso; solo importa dominio/puertos
└── infrastructure/
    ├── adapter/
    │   ├── in/web/     # Controlador REST, DTOs y mapper HTTP
    │   └── out/
    │       ├── persistence/ # SolicitudJpaEntity, repositorios Spring Data y mapper
    │       └── messaging/   # Adaptador Hablame
    ├── config/         # Beans y CORS
    └── exception/      # Manejo HTTP global de errores
```

`Solicitud` y `HistorialCambios` son clases Java puras. `SolicitudJpaEntity` y `HistorialCambiosJpaEntity`, ubicadas en `infrastructure.adapter.out.persistence`, son los modelos JPA y no se exponen como dominio. `domain` no depende de Spring/JPA; los servicios dependen de interfaces de `domain.port`, y la infraestructura implementa y conecta esos puertos mediante la configuración de Spring. Esta inversión permite probar las reglas sin infraestructura y reemplazar proveedores sin modificar el dominio.

## Ramas

El repositorio utiliza `main` para la integración estable y `develop` para el desarrollo.
