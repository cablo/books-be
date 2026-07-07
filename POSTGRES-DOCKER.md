# Postgres Docker

This project can run a local PostgreSQL database with the official `postgres` image.

## Start

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run-postgres.ps1
```

## Stop

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\stop-postgres.ps1
```

## App connection

- URL: `jdbc:postgresql://localhost:5432/app_db`
- Username: `app_user`
- Password: `app_password`

After Postgres is ready, run the app and Flyway will apply the migrations automatically.
