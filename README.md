# Sistema Bancario TUP 2024

Un sistema bancario completo desarrollado en Spring Boot que permite gestionar clientes, cuentas y préstamos bancarios.

## Características

- **Gestión de Clientes**: Registro de personas físicas y jurídicas
- **Cuentas Bancarias**: Caja de ahorro y cuenta corriente en pesos y dólares
- **Sistema de Préstamos**: Solicitud, evaluación crediticia y seguimiento de préstamos
- **API REST**: Endpoints documentados con Swagger
- **Base de Datos**: Persistencia con PostgreSQL y JPA

## Tecnologías

- Java 17
- Spring Boot 3.3.0
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger/OpenAPI

## Instalación

### Requisitos previos
- Java 17 o superior
- Maven 3.8+
- PostgreSQL 12+

### Configuración

1. **Clonar el repositorio**
   ```bash
   git clone [url-del-repositorio]
   cd tup2024
   ```

2. **Configurar la base de datos**
   ```sql
   -- Conectarse a PostgreSQL
   psql -U postgres
   
   -- Crear la base de datos
   CREATE DATABASE sistema_bancario;
   ```

3. **Configurar credenciales**

   ```bash
   # Copiar el archivo de ejemplo
   cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
   
   # Editar con tus credenciales
   nano src/main/resources/application-local.properties
   ```

### Profile Local
Para desarrollo local, usar el profile `local`:
```bash
mvn spring-boot:run -Dspring.profiles.active=local
```

4. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run -Dspring.profiles.active=local
   ```

La aplicación estará disponible en `http://localhost:8080`

## API Documentation

Una vez ejecutando la aplicación, puedes acceder a la documentación interactiva:
- **Swagger UI**: http://localhost:8080/swagger-ui.html

### Endpoints principales

#### Clientes
- `POST /cliente` - Crear nuevo cliente
- `GET /cliente` - Listar todos los clientes
- `GET /cliente/{id}` - Obtener cliente por ID

#### Cuentas
- `POST /cuenta` - Crear nueva cuenta
- `GET /cuenta/{id}` - Obtener cuentas de un cliente

#### Préstamos
- `POST /api/prestamo` - Solicitar préstamo
- `GET /api/prestamo/{clienteId}` - Consultar préstamos de un cliente

## Ejemplos de Uso

### Crear un cliente
```bash
curl -X POST http://localhost:8080/cliente \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Pepe",
    "apellido": "Rino",
    "dni": 12345678,
    "fechaNacimiento": "1990-01-15",
    "tipoPersona": "F",
    "banco": "Galicia"
  }'
```

### Crear una cuenta
```bash
curl -X POST http://localhost:8080/cuenta \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCliente": 12345678,
    "tipoCuenta": "CAJA_AHORRO",
    "moneda": "PESOS"
  }'
```

### Solicitar un préstamo
```bash
curl -X POST http://localhost:8080/api/prestamo \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCliente": 12345678,
    "plazoMeses": 12,
    "montoPrestamo": 50000.00,
    "moneda": "PESOS"
  }'
```

## Estructura del Proyecto

```
src/
├── main/java/ar/edu/utn/frbb/tup/
│   ├── controller/          # Controladores REST
│   ├── service/            # Lógica de negocio
│   ├── repository/         # Acceso a datos
│   ├── model/              # Entidades JPA
│   └── dto/                # Objetos de transferencia
└── test/                   # Tests unitarios
```
## Testing

Los tests incluyen:
- Validaciones de negocio
- Casos de error
- Flujos completos de préstamos
- Mocking de servicios externos

## Autor

**Joaquín Auday**  
📧 joaquin.auday@gmail.com

---

*Proyecto desarrollado para TUP 2024 - Universidad Tecnológica Nacional*