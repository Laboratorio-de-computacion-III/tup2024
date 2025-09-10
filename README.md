# 🏦 Sistema Bancario - TUP 2024

Aplicación en **Spring Boot** para gestionar clientes, cuentas y préstamos.

---

## 🚀 Configuración

### Prerrequisitos
- **Java** 21+
- **Maven** 3.8+
- **PostgreSQL** 12+

### Instalación

1. **Clonar el repositorio**
   ```bash
   git clone [url-del-repo]
   cd tup2024
   ```

2. **Crear la base de datos**
   ```bash
   psql -U postgres
   CREATE DATABASE sistema_bancario;
   \q
   ```

3. **Configurar credenciales**
   ```bash
   cp src/main/resources/application-local.properties.example       src/main/resources/application-local.properties
   ```
   Editar `application-local.properties` con usuario y contraseña de tu base.

4. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run -Dspring.profiles.active=local
   ```

---

## 📚 API

Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**Endpoints principales**
- `POST /cliente` → Crear cliente
- `POST /cuenta` → Crear cuenta
- `POST /api/prestamo` → Solicitar préstamo
- `GET /api/prestamo/{clienteId}` → Consultar préstamos

---

## 🏗️ Arquitectura

```bash
src/
├── controller/     # Controladores REST
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos (JPA)
├── model/          # Entidades
└── dto/            # DTOs
```

---

## 📝 Características

- ✅ Gestión de clientes (Físicos/Jurídicos)
- ✅ Cuentas en múltiples monedas (Pesos/Dólares)
- ✅ Préstamos con interés anual del 5%
- ✅ Validación crediticia
- ✅ Generación de plan de pagos automático

---

## 👤 Autor

**Joaquin Auday**  
📧 joaquin.auday@gmail.com
