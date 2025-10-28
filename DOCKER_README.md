# Docker Setup for TUP2024 Application

This guide explains how to run the TUP2024 Spring Boot application using Docker and Docker Compose.

## Prerequisites

- Docker Desktop installed (for Windows)
- Docker Compose (usually included with Docker Desktop)

## Quick Start

### 1. Build and Run with Docker Compose

Start the entire stack (PostgreSQL + Spring Boot app):

```bash
docker-compose up --build
```

This will:
- Build the Spring Boot application Docker image
- Start a PostgreSQL database container
- Start the Spring Boot application container
- Create a network for communication between containers

### 2. Access the Application

Once the containers are running:
- **Application API**: http://localhost:8080
- **PostgreSQL Database**: localhost:5432
  - Database: `tup`
  - Username: `postgres`
  - Password: `postgres`

### 3. Stop the Application

Stop and remove containers:

```bash
docker-compose down
```

Stop and remove containers + volumes (will delete database data):

```bash
docker-compose down -v
```

## Docker Commands

### Run in detached mode (background)
```bash
docker-compose up -d
```

### View logs
```bash
# All services
docker-compose logs -f

# Only app logs
docker-compose logs -f app

# Only database logs
docker-compose logs -f postgres
```

### Rebuild only the application
```bash
docker-compose up --build app
```

### Stop without removing containers
```bash
docker-compose stop
```

### Start existing containers
```bash
docker-compose start
```

### Check running containers
```bash
docker-compose ps
```

## Database Persistence

The PostgreSQL data is persisted in a Docker volume named `postgres_data`. This ensures your data survives container restarts.

To completely remove the database and start fresh:
```bash
docker-compose down -v
docker-compose up --build
```

## Troubleshooting

### Port already in use
If port 8080 or 5432 is already in use, modify the port mappings in `docker-compose.yml`:
```yaml
ports:
  - "8081:8080"  # Change host port to 8081
```

### Application not connecting to database
The application waits for PostgreSQL to be healthy before starting. If issues persist:
1. Check logs: `docker-compose logs postgres`
2. Verify database health: `docker-compose ps`

### Rebuild from scratch
```bash
docker-compose down -v
docker system prune -a
docker-compose up --build
```

## Architecture

- **Multi-stage Docker build**: Uses Maven to build the app, then copies only the JAR to a lightweight JRE image
- **Health checks**: PostgreSQL container has health checks to ensure it's ready before the app starts
- **Network isolation**: Services communicate via a dedicated Docker network
- **Volume persistence**: Database data persists across container restarts

## Environment Variables

You can customize the database connection by modifying environment variables in `docker-compose.yml`:

```yaml
environment:
  DB_URL: jdbc:postgresql://postgres:5432/tup
  DB_USERNAME: postgres
  DB_PASSWORD: postgres
```
