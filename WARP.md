# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

Project overview
- Java Spring Boot (parent: org.springframework.boot:spring-boot-starter-parent:3.3.0) REST API for a simple banking domain (clientes, cuentas).
- Layered architecture:
  - Web: REST controllers under `ar.edu.utn.frbb.tup.controller` (+ DTOs, validators, and a global exception handler returning `CustomApiError`).
  - Service: business logic in `ar.edu.utn.frbb.tup.service`.
  - Persistence: in-memory “poor man’s DB” (`AbstractBaseDao`) keyed by entity type; DAOs convert between domain models and persistence entities in `ar.edu.utn.frbb.tup.persistence.entity`.
  - Domain model: entities under `ar.edu.utn.frbb.tup.model` (Cliente, Cuenta, enums, and domain exceptions).
- Tests use JUnit 5 + Mockito (`spring-boot-starter-test`).

Common commands
- Build (run tests):
  ```powershell path=null start=null
  mvn clean verify
  ```
- Build (skip tests):
  ```powershell path=null start=null
  mvn clean package -DskipTests
  ```
- Run the application (HTTP server):
  ```powershell path=null start=null
  mvn spring-boot:run
  ```
- Run tests:
  ```powershell path=null start=null
  mvn test
  ```
- Run a single test class:
  ```powershell path=null start=null
  mvn -Dtest=ClienteServiceTest test
  ```
- Run a single test method:
  ```powershell path=null start=null
  mvn -Dtest=ClienteServiceTest#testAgregarCuentaAClienteDuplicada test
  ```

Key architecture notes
- Controllers
  - `ClienteController` exposes `POST /cliente` with `ClienteDto`; input is validated by `ClienteValidator` before delegating to `ClienteService`.
  - `TupResponseEntityExceptionHandler` maps exceptions to JSON `CustomApiError` and HTTP statuses (e.g., BAD_REQUEST for validation/`TipoCuentaAlreadyExistsException`).
- Services
  - `ClienteService` handles client creation and account linking; enforces age >= 18 and uniqueness by DNI via `ClienteDao`.
  - `CuentaService` guards duplicate account numbers via `CuentaDao`, delegates ownership checks to `ClienteService`, then persists.
- Persistence
  - `AbstractBaseDao` holds a static in-memory map per entity name. `ClienteDao` and `CuentaDao` store `ClienteEntity`/`CuentaEntity` records and translate to/from domain models.
  - `ClienteDao.find(dni, loadComplete)` optionally assembles the client with its cuentas via `CuentaDao.getCuentasByCliente(dni)`.
- Domain
  - `Cliente` owns a `Set<Cuenta>` and enforces one account per (tipoCuenta, moneda) via `tieneCuenta`.
  - `Cuenta` has random `numeroCuenta`, `TipoCuenta`, `TipoMoneda`, and balance operations that throw domain exceptions.

Testing
- Unit tests are under `src/test/java`. `ClienteServiceTest` uses Mockito to stub `ClienteDao` and covers: underage client rejection, duplicate client by DNI, adding accounts, and duplicate account per (tipo, moneda).

Notes for agents
- No formatter/linter plugins are configured in Maven; rely on Maven build and tests. If needed, propose adding Checkstyle/Spotless in a separate change.
- Spring Boot 3.x is used (per `pom.xml`); ensure a compatible JDK is available when running locally.
