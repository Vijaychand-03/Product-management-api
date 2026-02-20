## Product Management API

RESTful API for managing products and their items, built with **Java 17**, **Spring Boot**, **Spring Data JPA**, **PostgreSQL**, and secured with **Spring Security (JWT + refresh tokens)**.

### Tech Stack

- **Backend**: Spring Boot, Spring Web, Spring Data JPA, Spring Security
- **Database**: PostgreSQL (H2 in-memory for tests)
- **Security**: JWT access token + refresh token, role-based authorization
- **Docs**: Swagger / OpenAPI via springdoc-openapi
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Containerization**: Docker, Docker Compose

### Architecture Overview

- `entity`: JPA entities (`Product`, `Item`, `User`, `RefreshToken`, `Role`)
- `repository`: Spring Data JPA repositories
- `dto`: Request/response models for API
- `service`: Business logic for products, items, auth, and refresh tokens
- `controller`: REST controllers (`/api/v1/products`, `/api/v1/auth`)
- `security`: JWT generation/validation, filters, security configuration
- `exception`: Centralized exception handling and API error model
- `config`: OpenAPI configuration

### Main Features

- Full CRUD on products (`/api/v1/products`) with pagination
- Nested items endpoint (`/api/v1/products/{id}/items`)
- JWT authentication with refresh token rotation (`/api/v1/auth/login`, `/api/v1/auth/refresh`)
- Role-based access (`ROLE_USER`, `ROLE_ADMIN`)
- Global error handling with standardized JSON error responses
- CORS configuration for common front-end origins

### Running Locally (without Docker)

1. Ensure **Java 17+** and **Maven** are installed.
2. Start PostgreSQL locally and create a database:
   - DB name: `product_db`
   - User: `postgres`
   - Password: `123456`
3. Adjust `src/main/resources/application.properties` if needed for your local DB credentials.
4. From the project root, run:

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

### Running with Docker & Docker Compose

1. Ensure **Docker** and **Docker Compose** are installed.
2. From the project root, run:

```bash
docker-compose up --build
```

3. The API will be available at `http://localhost:8080`.

Docker Compose starts:

- `db`: PostgreSQL 16 with database `product_db`
- `app`: Spring Boot application connected to `db`

### API Documentation (Swagger / OpenAPI)

- After the application starts, open:
  - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
  - OpenAPI spec: `http://localhost:8080/v3/api-docs`

### Security & Authentication

- JWT-based authentication with:
  - **Access token** (short-lived)
  - **Refresh token** (long-lived, rotated and stored server-side)
- Endpoints:
  - `POST /api/v1/auth/login` – returns `accessToken` and `refreshToken`
  - `POST /api/v1/auth/refresh` – accepts `refreshToken` and returns new access token

Protected resources:

- `GET /api/v1/products/**` – requires `ROLE_USER` or `ROLE_ADMIN`
- `POST/PUT/DELETE /api/v1/products/**` – requires `ROLE_ADMIN`

Configure JWT properties in `application.properties`:

- `app.jwt.secret` – Base64-encoded HMAC secret
- `app.jwt.access-token-validity-ms`
- `app.jwt.refresh-token-validity-ms`

### Database Schema

Core tables match the assignment:

- `product(id, product_name, created_by, created_on, modified_by, modified_on)`
- `item(id, product_id, quantity, ...)`

Additional tables for security:

- `users`, `user_roles`, `refresh_token`

### Testing

- **Unit tests**: service layer (e.g. `ProductServiceTest`)
- **Integration tests**: controller endpoints with full context (`ProductControllerIT`)
- Uses **H2 in-memory database** by default in tests via Spring Boot test auto-configuration.

Run tests with:

```bash
mvn test
```

### Pagination & Error Handling

- Collection endpoints use Spring `Pageable`:
  - `GET /api/v1/products?page=0&size=10&sort=productName,asc`
- Errors return a standardized JSON structure (`ApiError`) with:
  - `timestamp`, `status`, `error`, `message`, `path`, `details`

### CORS & HTTPS

- CORS is configured to allow typical local front-end origins (`http://localhost:3000`, `http://localhost:4200`).
- `server.forward-headers-strategy=framework` prepares the app for running behind HTTPS reverse proxies in containerized environments.

