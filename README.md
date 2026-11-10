# TechStore Backend

API REST de **TechStore** (tienda de tecnología) desarrollada con Spring Boot para la asignatura
**DSY1104 Desarrollo Fullstack II, Evaluación Parcial 3**. Se conecta a una base de datos MySQL,
expone endpoints versionados (`/api/v1`), documenta la API con Swagger y protege los recursos con
**Spring Security + JWT** según el rol del usuario.

El frontend (React) está en el repositorio `techstore-react`.

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 17 + Spring Boot 4.1 | Aplicación y API REST |
| Spring Data JPA / Hibernate | Acceso a datos |
| MySQL 8 | Base de datos de la aplicación |
| H2 (en memoria) | Base de datos de las pruebas |
| Spring Security + JJWT | Autenticación con tokens JWT y roles |
| springdoc-openapi | Documentación Swagger |
| Lombok | Reducción de código repetitivo |
| JUnit 5 | Pruebas |

## Requisitos

- JDK 17
- MySQL 8 en ejecución (puerto 3306)
- IntelliJ IDEA (recomendado) con **Annotation Processing** activado (Lombok)
- Postman (opcional, para probar los endpoints)

## Cómo ejecutarlo

### 1. Crear la base de datos

```sql
CREATE DATABASE techstore;
```

Las tablas las crea Hibernate al iniciar la aplicación (`ddl-auto: update`).

### 2. Configurar las variables de entorno

La aplicación **no guarda secretos en el código**: los lee desde variables de entorno.

| Variable | Obligatoria | Descripción | Valor por defecto |
|---|---|---|---|
| `DB_PASSWORD` | Sí | Contraseña del usuario de MySQL | — |
| `JWT_SECRET` | Sí | Clave secreta para firmar tokens (Base64, mínimo 32 bytes) | — |
| `DB_USERNAME` | No | Usuario de MySQL | `root` |
| `DB_URL` | No | URL JDBC | `jdbc:mysql://localhost:3306/techstore?...` |
| `JWT_EXPIRACION_MS` | No | Duración del token en milisegundos | `3600000` (1 hora) |

Para generar una clave JWT aleatoria (PowerShell):

```powershell
[Convert]::ToBase64String((1..48 | ForEach-Object { Get-Random -Maximum 256 }) -as [byte[]])
```

**En IntelliJ:** `Run > Edit Configurations... > TechstoreBackendApplication > Environment variables`
y escribir, por ejemplo: `DB_PASSWORD=tu_clave;JWT_SECRET=la_clave_generada`.

Si falta alguna variable obligatoria, la aplicación no inicia y el error indica
`Could not resolve placeholder 'DB_PASSWORD'` (o `JWT_SECRET`).

### 3. Iniciar la aplicación

Ejecutar la clase `TechstoreBackendApplication` desde IntelliJ, o por terminal:

```powershell
$env:DB_PASSWORD = "tu_clave"
$env:JWT_SECRET = "la_clave_generada"
.\mvnw.cmd spring-boot:run
```

La API queda en `http://localhost:8080`.

> **Nota IntelliJ:** si falla la sincronización de Maven, en `Settings > Build Tools > Maven`
> usar **Maven home path: Bundled (Maven 3)** y en `Project Structure` un SDK 17.

## Documentación de la API (Swagger)

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- Especificación OpenAPI (JSON): <http://localhost:8080/v3/api-docs>
- Tabla de endpoints: [`docs/API.md`](docs/API.md)

Para probar endpoints protegidos en Swagger: hacer login (`POST /api/v1/auth/login`), copiar el
`token` de la respuesta, presionar **Authorize** y pegarlo (sin la palabra "Bearer").

## Usuarios de demostración

Se crean automáticamente al iniciar la aplicación si no existen (`DataInitializer`).
Son datos de ejemplo para desarrollo y pruebas.

| Rol | Correo | Contraseña |
|---|---|---|
| Administrador | `admin@techstore.cl` | `admin123` |
| Vendedor | `vendedor@techstore.cl` | `vendedor123` |
| Cliente | Se crea con `POST /api/v1/auth/registro` | La elegida al registrarse |

## Roles y permisos

| Rol | Permisos |
|---|---|
| **ADMIN** | Acceso total al sistema |
| **VENDEDOR** | Ver lista y detalle de productos; ver lista y detalle de órdenes. No accede a nada más |
| **CLIENTE** | Solo la tienda: ver catálogo y comprar |

El catálogo (`GET` de productos y categorías) es público. Crear, modificar y eliminar requiere
rol ADMIN. Una petición sin token recibe **401**; con token pero sin permiso, **403**.

## Pruebas

```powershell
.\mvnw.cmd test
```

Las pruebas usan H2 en memoria y una clave JWT de prueba (`src/test/resources/application.yaml`),
por lo que **no necesitan MySQL ni variables de entorno**.

## Estructura del proyecto

```
src/main/java/cl/duoc/techstore/techstore_backend/
├── config/       Seguridad, Swagger y datos iniciales
├── controller/   Endpoints REST
├── dto/          Objetos de entrada y salida de la API
├── exception/    Excepciones del negocio
├── model/        Entidades JPA y enumeración de roles
├── repository/   Acceso a la base de datos (Spring Data)
├── security/     Filtro JWT
└── service/      Lógica de negocio
```

Flujo de una petición: **Controller → Service → Repository → Base de datos**.

## Trabajo en equipo y Git

- La rama `main` está protegida: todos los cambios entran por **Pull Request**.
- Una rama por funcionalidad: `feature/usuarios`, `feature/boletas`, `feature/refresh-token`...
- Mensajes de commit claros, con prefijo: `feat:`, `fix:`, `docs:`, `test:`, `refactor:`.
- Antes del primer commit, configurar la identidad:

```powershell
git config user.name "Nombre Apellido"
git config user.email "correo@ejemplo.cl"
```

- **Nunca** subir contraseñas, claves ni archivos `.env` (el `.gitignore` ya excluye los más comunes).

## Equipo

- Esteban Varas
- Víctor Cortez
