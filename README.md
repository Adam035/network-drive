# Network Drive

Network Drive exposes files stored on a server as a drive in Windows. The project consists of three applications:

- **Backend** - a Spring Boot API responsible for authentication, authorization, file metadata, and file content.
- **Frontend** - a React application for registration, login, account management, and the desktop authorization flow.
- **Desktop** - a Windows application that mounts a virtual drive through WinFsp and translates file system operations into backend API requests.

PostgreSQL stores metadata, MinIO stores file content, and Redis stores short-lived desktop authorization codes.

## Architecture

| Directory | Main technologies | Responsibility |
| --- | --- | --- |
| `backend/` | Java 21, Spring Boot 3.5.7, Spring Security, JPA | Authentication, authorization, storage API, users, volumes, and metadata. |
| `frontend/` | React 19, TypeScript, Vite 8, Tailwind CSS, DaisyUI | Registration, login, profile, and desktop authorization pages. |
| `desktop/` | Java 21, Spring Boot 4.1.0, WinFsp, JNA | Windows virtual drive and HTTP client for the backend. |

Both Java applications separate domain models, application use cases, ports, and infrastructure adapters. In the desktop application, WinFsp adapters handle Windows file system calls while network adapters call the backend. In the backend, infrastructure adapters connect application logic to HTTP, PostgreSQL, Redis, MinIO, and JWT.

## Requirements

### Backend

- Java 21 for local execution.
- PostgreSQL for users and file metadata.
- Redis for short-lived desktop authorization attempts.
- MinIO for file content.
- A stable JWT signing secret.
- Docker and Docker Compose if running the provided Compose configuration.

### Frontend

- Node.js compatible with the Vite version declared in `frontend/package.json`.
- npm.
- A reachable backend URL.
- A browser with WebAuthn support.

### Desktop

- Windows with WinFsp installed.
- Java 21 for development.
- Network access to the backend and frontend.
- A browser with WebAuthn support.

The repository does not include DNS, TLS certificate, or reverse proxy configuration for the `network-drive.local` domains used by the current application settings.

## Repository structure

```text
network-drive/
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/io/github/adam035/networkdrive/
│       └── resources/application.yml
├── frontend/
│   ├── package.json
│   ├── vite.config.ts
│   └── src/
├── desktop/
│   ├── pom.xml
│   └── src/main/
│       ├── java/io/github/adam035/desktopfs/
│       └── resources/application.yaml
└── docker-compose.yml
```

## Backend

### Responsibilities

The backend provides:

- WebAuthn registration and login.
- JWT access and refresh tokens.
- Authorization codes for the desktop application.
- User and volume information.
- File and directory operations.
- Metadata persistence in PostgreSQL.
- File content storage in MinIO.

The `domain` package contains the core models and repository interfaces. The `application` package contains use cases, commands, results, and ports. The `infrastructure` package contains Spring controllers, security filters, persistence implementations, and external service adapters.

### Configuration

`backend/src/main/resources/application.yml` reads these environment variables:

| Variable | Purpose |
| --- | --- |
| `DB_HOST` | PostgreSQL host. |
| `DB_PORT` | PostgreSQL port. |
| `DB_NAME` | PostgreSQL database name. |
| `DB_USERNAME` | PostgreSQL username. |
| `DB_PASSWORD` | PostgreSQL password. |
| `REDIS_HOST` | Redis hostname or IP address, without a URL scheme or port. |
| `REDIS_PORT` | Redis port. |
| `REDIS_PASSWORD` | Redis password. |
| `MINIO_ENDPOINT` | Complete MinIO API URL, including scheme and port. |
| `MINIO_BUCKET` | Bucket used for file content. |
| `MINIO_ACCESS_KEY` | MinIO access key. |
| `MINIO_SECRET_KEY` | MinIO secret key. |
| `JWT_SECRET` | Stable Base64-encoded HMAC signing secret. |

The JWT secret must remain the same across backend restarts and across backend instances. Changing it invalidates previously issued tokens. Do not commit passwords or signing secrets.

The backend currently uses `spring.jpa.hibernate.ddl-auto: update`. Review this setting before using the application with important data.

### Data stores

**PostgreSQL** stores users, WebAuthn credentials, shares, files, directories, storage resources, and other metadata managed through JPA.

**MinIO** stores file bytes under object keys associated with file metadata. At startup, the backend checks whether its configured bucket exists and creates it when necessary.

**Redis** stores desktop authorization attempts under keys beginning with `oauth2:code:`. These entries expire after two minutes. Redis repository scanning is disabled because this application uses a typed `RedisTemplate` rather than Redis repository interfaces.

### Main API routes

| Method and path | Purpose |
| --- | --- |
| `POST /api/auth/register/start` | Start WebAuthn registration. |
| `POST /api/auth/register/finish` | Complete WebAuthn registration. |
| `POST /api/auth/login/start` | Start WebAuthn login. |
| `POST /api/auth/login/finish` | Complete WebAuthn login and issue tokens. |
| `POST /api/auth/oauth2` | Issue a desktop authorization code after browser login. |
| `POST /api/auth/token/exchange-code` | Exchange an authorization code and PKCE verifier for tokens. |
| `POST /api/auth/token/rotate` | Rotate tokens using a `refreshToken` cookie. |
| `GET /api/volumes` | List volumes available to the authenticated user. |
| `GET /api/volumes/{volumeLabel}` | Get volume information. |
| `GET /api/user/{userId}` | Get user details. |
| `PUT /api/user/{userId}` | Update user details. |
| `POST /directories` | Create a directory. |
| `GET /directories/**` | Read a directory. |
| `POST /files` | Upload file content. |
| `GET /files/**` | Download file content. |
| `GET /storage-resources/**` | Read resource metadata. |
| `PATCH /storage-resources/move` | Move a resource. |
| `PATCH /storage-resources/**` | Update a security descriptor. |
| `DELETE /storage-resources/**` | Delete a resource. |

The file, directory, and storage resource routes do not use the `/api` prefix.

Spring Security allows requests under `/api/auth/**` to reach the controllers. Other routes require authentication. An authorization endpoint may still require an authenticated user in its application logic even when its path is configured with `permitAll()`.

### JWT and cookies

The backend generates access tokens with a five-minute lifetime and refresh tokens with a one-day lifetime. Browser login and token rotation return tokens in the response and set `HttpOnly`, `Secure`, `SameSite=None` cookies.

The frontend uses the refresh token cookie when calling the rotation endpoint. The desktop application stores its refresh token locally and sends it explicitly as a `Cookie` header when calling the same endpoint.

## Frontend

The frontend is a React single-page application built with TypeScript and Vite. Its routes include:

| Route | Purpose |
| --- | --- |
| `/auth/register` | WebAuthn registration. |
| `/auth/login` | WebAuthn login. |
| `/auth/callback` | Completes the browser side of desktop authorization. |
| `/profile` | Protected user profile. |

`AuthProvider` keeps the current access and refresh tokens in React state. During initialization, it attempts to rotate tokens using the browser's refresh token cookie. `ProtectedRoute` redirects users without a valid access token to the login page.

### Frontend configuration

| Variable | Purpose |
| --- | --- |
| `VITE_BACKEND_URL` | Base URL used for backend API requests. |
| `VITE_APP_NAME` | Application name shown in the UI. |
| `VITE_LIGHT_THEME` | Optional light DaisyUI theme. |
| `VITE_DARK_THEME` | Optional dark DaisyUI theme. |

Vite exposes `VITE_*` values to browser code. Do not put secrets in these variables.

The backend CORS configuration currently allows `https://network-drive.local`. Running Vite at a different origin, such as `http://localhost:5173`, requires an appropriate local CORS or reverse proxy configuration.

### Local commands

From `frontend/`:

```bash
npm ci
npm run dev
```

To check the production build and linting:

```bash
npm run build
npm run lint
```

The Vite development server is configured to listen on port `5173`.

## Desktop

The desktop application implements a Windows file system using WinFsp. Windows file operations enter the WinFsp adapter, pass through application use cases, and become backend HTTP requests.

The implementation includes operations for opening, creating, reading, writing, flushing, renaming, listing, and deleting resources, as well as handling Windows security descriptors. The application selects a free drive letter starting at `Z:` and working downward.

### Desktop configuration

`desktop/src/main/resources/application.yaml` defines these settings:

| Environment variable | Purpose | Current default |
| --- | --- | --- |
| `NETWORK_DRIVE_BASE_URL` | Backend base URL. | `http://192.168.0.46:8085` |
| `NETWORK_DRIVE_LOGIN_PAGE_URL` | Browser login URL. | `https://network-drive.local/auth/login` |
| `STORAGE_RESOURCES_EXPIRE_AFTER_WRITE` | Resource cache lifetime in seconds. | `60` |
| `STORAGE_RESOURCES_MAXIMUM_SIZE` | Maximum resource cache entries. | `10000` |
| `DIRECTORY_LISTINGS_EXPIRE_AFTER_WRITE` | Directory listing cache lifetime in seconds. | `60` |
| `DIRECTORY_LISTINGS_MAXIMUM_SIZE` | Maximum directory listing cache entries. | `10000` |

### Local credentials

The desktop application uses Windows DPAPI to protect locally stored values. The secure store writes files under the current user's `AppData\Local\NetworkDrive` directory, including:

- `refresh-token.dpapi`
- `code-verifier.dpapi`
- `state.dpapi`

The access token is held in process memory for authenticated API requests. The refresh token is persisted and can be used to obtain new tokens on a later launch.

### Caching

Caffeine caches storage resource metadata and directory listings. Cache size and expiration are configurable through the settings above. Some write operations evict affected cache entries so subsequent reads can fetch updated data from the backend.

## Desktop authentication flow

1. At startup, the desktop application checks whether it received an authentication callback URL.
2. On a regular launch with a stored refresh token, it calls `POST /api/auth/token/rotate`.
3. Without a refresh token, it generates a PKCE verifier and a random `state`, saves them through DPAPI, and opens the frontend login page.
4. The user authenticates in the browser with WebAuthn.
5. The frontend sends the desktop authorization request to `POST /api/auth/oauth2`, including the browser access token.
6. The backend creates a short-lived authorization code and stores the corresponding attempt in Redis for two minutes.
7. The frontend callback uses the `network-drive://callback` deeplink to deliver `code` and `state` to the desktop application.
8. The desktop verifies `state` against its local value and sends the code and PKCE verifier to `POST /api/auth/token/exchange-code`.
9. The backend loads the authorization attempt from Redis, checks the PKCE challenge, and returns access and refresh tokens.
10. The desktop saves the refresh token, obtains an access token for API calls, requests the available volumes, and attempts to mount them through WinFsp.

The authorization code, `state`, and PKCE verifier must belong to the same login attempt. Starting another login attempt replaces the desktop's locally stored verifier and state. A code that has expired in Redis cannot be exchanged.

## Running the services

### Docker Compose

The repository's `docker-compose.yml` defines:

- `backend`, published on host port `8080`
- `postgres`, published on host port `5432`
- `redis`, published on host port `6379`
- `minio`, with API on host port `9000` and console on `9001`

Before running Compose, create the environment files it references:

| File | Required configuration |
| --- | --- |
| `backend/.env` | Backend variables listed above. |
| `database/.env` | PostgreSQL database name, user, and password. |
| `redis/.env` | `REDIS_PASSWORD`. |
| `storage/.env` | MinIO root user and password. |

The current repository tree does not contain the `database/`, `redis/`, or `storage/` directories. Create them and their environment files before using the Compose file. Ensure the database and MinIO credentials agree with the values supplied to the backend.

From the repository root:

```bash
docker compose up --build -d
docker compose ps
docker compose logs -f backend
```

Inside the Compose network, the backend can use `postgres`, `redis`, and `minio` as hostnames. When a process runs directly on the host, use addresses reachable from the host instead.

Compose does not include the frontend. It also does not define health checks, so startup order alone does not guarantee that PostgreSQL, Redis, and MinIO are ready when the backend starts.

### Backend outside Docker

Start PostgreSQL, Redis, and MinIO first, then provide the backend environment variables to the Java process or IDE run configuration.

From `backend/` on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

An `.env` file is not automatically loaded into an IDE-launched Spring Boot process. Its values must be supplied as process environment variables or loaded by an explicit configuration mechanism.

### Desktop during development

Run `io.github.adam035.desktopfs.DesktopFileSystemApplication` with Java 21 on Windows. Make sure WinFsp is installed and the configured backend and frontend URLs are reachable. Spring Boot logs are written to the console; the current desktop configuration does not specify a log file.

## Testing

Run the available Java tests from their respective directories:

```powershell
cd backend
.\mvnw.cmd test

cd ..\desktop
.\mvnw.cmd test
```

Check the frontend build and lint rules:

```powershell
cd frontend
npm run build
npm run lint
```

These commands do not replace a manual test of WebAuthn, code exchange, token rotation, and WinFsp mounting. Those operations depend on a browser, Windows, and running external services.

## Diagnostics

### Redis authorization codes

The backend stores authorization attempts under keys beginning with `oauth2:code:`. They expire after two minutes. To inspect them in a development environment:

```powershell
docker exec -it network-drive-redis redis-cli --askpass
```

Then run:

```text
SCAN 0 MATCH oauth2:code:* COUNT 100
GET oauth2:code:<code>
TTL oauth2:code:<code>
```

If `SCAN` returns a cursor other than `0`, continue scanning with that cursor.

### Common problems

| Symptom | First things to check |
| --- | --- |
| An unresolved `${VARIABLE}` placeholder | Verify that the variable exists in the environment of the process that starts the application. |
| Redis connection failure | Check the host, port, password, and whether the caller runs inside or outside the Compose network. |
| Authorization code not found | Check its two-minute TTL, the Redis instance, and whether the callback code exactly matches the Redis key. |
| PKCE verification failure | Confirm that both sides use the same textual verifier and `Base64URL(SHA-256(UTF-8(verifier)))`. |
| JWT signature mismatch | Confirm that the backend uses the same stable `JWT_SECRET` as when the token was issued. |
| `401` during token rotation | Check that the refresh token is sent in the `refreshToken` cookie. |
| No mounted drive | Check whether `GET /api/volumes` returns labels, whether the access token is present, WinFsp logs, and available drive letters. |
| CORS error in the browser | Check the frontend origin against the origin allowed by the backend. |
