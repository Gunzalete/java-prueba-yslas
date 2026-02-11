# Prueba Técnica Java

### Requisitos

- Java 17+
- Maven 3.8+
- Docker y Docker Compose (opcional, para ejecutar con contenedores)

### Pasos para correr el sitio

Clonar el proyecto, situarse en la carpeta del sitio y correr:

1) `docker compose up --build`
2) Abrir `http://localhost:8080`

Si es la primera vez, el contenedor construye la aplicación y la ejecuta automáticamente.

Alternativamente, sin Docker:
```bash
./mvnw spring-boot:run
```

### Endpoints de la API

**Productos:**
- `POST /api/products` - Crear producto
- `GET /api/products` - Listar productos
- `GET /api/products/{id}` - Obtener producto
- `PUT /api/products/{id}` - Actualizar producto
- `DELETE /api/products/{id}` - Eliminar producto

**Órdenes:**
- `POST /api/orders` - Crear orden
- `GET /api/orders` - Listar órdenes
- `GET /api/orders/{id}` - Obtener orden
- `PUT /api/orders/{id}` - Actualizar orden
- `DELETE /api/orders/{id}` - Eliminar orden

### Filtros

**Productos:**
- `name` = búsqueda parcial por nombre
- `minPrice` = precio mínimo
- `maxPrice` = precio máximo
- `per_page` = 1..100 (default: 10)

**Órdenes:**
- `orderNumber` = número de orden
- `fromDate` = fecha de inicio (YYYY-MM-DD)
- `toDate` = fecha de fin (YYYY-MM-DD)
- `per_page` = 1..100 (default: 10)

### Reglas de negocio

- El precio de un producto debe ser mayor a 0
- El total de una orden se calcula automáticamente en el servidor
- La fecha de una orden no puede ser futura; si no se envía, se usa la fecha actual
- Cada orden debe tener al menos un item
- No se pueden crear órdenes con productos inexistentes

Formato de errores de negocio (HTTP 400/422) — ejemplo:

```json
{
	"status": 400,
	"message": "Validation failed",
	"errors": {
		"name": ["El nombre es requerido"],
		"price": ["El precio debe ser mayor a 0"]
	}
}
```

### Probar con Postman

1) Crear una coleccion nueva.
2) Agregar una variable de coleccion `baseUrl` con valor `http://localhost:8080`.
3) Crear requests con `Accept: application/json` y `Content-Type: application/json`.

También puedes importar la colección `Postman_Collection.json` incluida en este repositorio para probar rápidamente los endpoints.

#### Crear producto

- Método: `POST`
- URL: `{{baseUrl}}/api/products`
- Body (raw JSON):

```json
{
	"name": "Laptop",
	"price": 999.99,
	"description": "High-performance laptop"
}
```

#### Listar productos (con filtros)

- Método: `GET`
- URL: `{{baseUrl}}/api/products?name=laptop&minPrice=500&maxPrice=1500&size=5`

#### Obtener producto por ID

- Método: `GET`
- URL: `{{baseUrl}}/api/products/1`

#### Actualizar producto

- Método: `PUT`
- URL: `{{baseUrl}}/api/products/1`
- Body (raw JSON):

```json
{
	"name": "Laptop Pro",
	"price": 1299.99,
	"description": "Updated description"
}
```

#### Crear orden

- Método: `POST`
- URL: `{{baseUrl}}/api/orders`
- Body (raw JSON):

```json
{
	"orderNumber": 1001,
	"items": [
		{
			"productId": 1,
			"quantity": 2
		}
	]
}
```

#### Listar órdenes (con filtros)

- Método: `GET`
- URL: `{{baseUrl}}/api/orders?orderNumber=1001&fromDate=2024-01-01&toDate=2024-12-31&size=5`

#### Obtener orden por ID

- Método: `GET`
- URL: `{{baseUrl}}/api/orders/1`

Respuesta esperada (ejemplo):

```json
{
	"id": 1,
	"orderNumber": 1001,
	"orderDate": "2026-02-11",
	"total": 1999.98,
	"items": [
		{
			"id": 1,
			"productId": 1,
			"productName": "Laptop",
			"quantity": 2,
			"price": 999.99
		}
	]
}
```

#### Actualizar orden

- Método: `PUT`
- URL: `{{baseUrl}}/api/orders/1`
- Body (raw JSON):

```json
{
	"orderNumber": 1001,
	"items": [
		{
			"productId": 1,
			"quantity": 3
		}
	]
}
```

### Tests

```bash
./mvnw test
```

Ejecuta los tests unitarios del proyecto. Se incluyen tests para:
- Servicios (OrderService, ProductService)
- Controllers (OrderController)
- Validaciones de negocio

### Características principales

- **Spring Boot 3.5.10** - Framework web
- **JPA/Hibernate** - ORM para persistencia
- **Validation** - Validación declarativa con anotaciones
- **H2 Database** - Base de datos en memoria
- **Docker** - Containerización de la aplicación
- **Postman Collection** - Colección lista para importar

### Estructura del proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/PruebaTecnicaJavaYslas/
│   │       ├── controller/        # REST Controllers
│   │       ├── dto/               # Data Transfer Objects
│   │       ├── model/             # Entidades JPA
│   │       ├── repository/        # Spring Data Repositories
│   │       ├── service/           # Lógica de negocio
│   │       ├── exception/         # Excepciones personalizadas
│   │       └── mapper/            # Mappers DTO ↔ Entity
│   └── resources/
│       └── application.properties # Configuración
└── test/
    └── java/                      # Tests unitarios
```
