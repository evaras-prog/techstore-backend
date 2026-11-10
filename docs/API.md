# Documentación de la API — TechStore

| | |
|---|---|
| **Versión del documento** | 1.0 |
| **URL de la API** | `http://localhost:8080/api/v1` |
| **URL de Swagger** | `http://localhost:8080/swagger-ui/index.html` |
| **Tipo de API** | Pública y privada (según el endpoint) |
| **Autenticación** | JWT en el header `Authorization: Bearer <token>` |
| **Formato** | JSON |

Formato basado en el Anexo 3 de la evaluación. La columna **Responsable** indica quién implementa
cada endpoint pendiente.

## Autenticación — `/api/v1/auth`

| Método | Ruta | Descripción | Datos de entrada | Autenticación | Roles | Respuestas | Observaciones |
|---|---|---|---|---|---|---|---|
| POST | `/api/v1/auth/registro` | Registra un usuario con rol CLIENTE y devuelve su token | `{nombre, email, password}` | No | Público | 201 Creado, 400 Datos inválidos, 409 Email ya registrado | La contraseña se guarda cifrada (BCrypt) y debe tener 6 caracteres o más |
| POST | `/api/v1/auth/login` | Inicia sesión y devuelve un token JWT | `{email, password}` | No | Público | 200 Éxito, 400 Datos inválidos, 401 Credenciales inválidas | Mismo mensaje de error si el email no existe o la clave es incorrecta |
| GET | `/api/v1/auth/yo` | Devuelve el correo y los roles del usuario del token | — | Sí | Cualquier rol | 200 Éxito, 401 Sin token o token inválido | Útil para verificar la sesión |

## Categorías — `/api/v1/categorias`

| Método | Ruta | Descripción | Datos de entrada | Autenticación | Roles | Respuestas | Observaciones |
|---|---|---|---|---|---|---|---|
| GET | `/api/v1/categorias` | Lista todas las categorías | — | No | Público | 200 Éxito | Catálogo de la tienda |
| GET | `/api/v1/categorias/{id}` | Detalle de una categoría | — | No | Público | 200 Éxito, 404 No encontrada | |
| POST | `/api/v1/categorias` | Crea una categoría | `{nombre}` | Sí | ADMIN | 201 Creado, 400 Datos inválidos, 401, 403 | `nombre` obligatorio |
| PUT | `/api/v1/categorias/{id}` | Actualiza una categoría | `{nombre}` | Sí | ADMIN | 200 Éxito, 400, 401, 403, 404 | |
| DELETE | `/api/v1/categorias/{id}` | Elimina una categoría | — | Sí | ADMIN | 204 Sin contenido, 401, 403, 404, 409 En uso | 409 cuando la categoría tiene productos |

## Productos — `/api/v1/productos`

| Método | Ruta | Descripción | Datos de entrada | Autenticación | Roles | Respuestas | Observaciones |
|---|---|---|---|---|---|---|---|
| GET | `/api/v1/productos` | Lista todos los productos | — | No | Público | 200 Éxito | Incluye su categoría |
| GET | `/api/v1/productos/{id}` | Detalle de un producto | — | No | Público | 200 Éxito, 404 No encontrado | |
| POST | `/api/v1/productos` | Crea un producto | `{nombre, precio, stock, categoria: {id}}` | Sí | ADMIN | 201 Creado, 400 Datos inválidos, 401, 403, 404 Categoría inexistente | `precio` mayor a 0; `stock` mayor o igual a 0 |
| PUT | `/api/v1/productos/{id}` | Actualiza un producto | `{nombre, precio, stock, categoria: {id}}` | Sí | ADMIN | 200 Éxito, 400, 401, 403, 404 | |
| DELETE | `/api/v1/productos/{id}` | Elimina un producto | — | Sí | ADMIN | 204 Sin contenido, 401, 403, 404 | |

## Endpoints pendientes

| Método | Ruta | Descripción | Roles | Responsable |
|---|---|---|---|---|
| POST | `/api/v1/auth/refresh` | Renueva el token de un usuario con sesión vigente | Cualquier rol | Esteban |
| GET, POST, PUT, DELETE | `/api/v1/usuarios` | Gestión de usuarios (crear vendedores, cambiar roles) | ADMIN | Víctor |
| GET | `/api/v1/boletas`, `/api/v1/boletas/{id}` | Lista y detalle de órdenes | ADMIN, VENDEDOR | Víctor |
| POST | `/api/v1/boletas` | Crea una orden (compra) y descuenta stock | CLIENTE, ADMIN | Víctor |

## Códigos de respuesta

| Código | Significado |
|---|---|
| 200 | Éxito |
| 201 | Creado |
| 204 | Éxito sin contenido |
| 400 | Datos inválidos |
| 401 | No autenticado (sin token, inválido o vencido) |
| 403 | Autenticado, pero sin permiso para el recurso |
| 404 | Recurso no encontrado |
| 409 | Conflicto (por ejemplo, email ya registrado) |
