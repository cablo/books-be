$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$composeFile = Join-Path $projectRoot "docker-compose.postgres.yml"

if (-not (Test-Path $composeFile)) {
    throw "Compose file not found: $composeFile"
}

Write-Host "Starting Postgres container from $composeFile..."
docker compose -f $composeFile up -d

Write-Host ""
Write-Host "Postgres is starting in Docker."
Write-Host "Connection details for the app:"
Write-Host "  URL:      jdbc:postgresql://localhost:5432/app_db"
Write-Host "  Username: app_user"
Write-Host "  Password: app_password"
Write-Host ""
Write-Host "Check container status with:"
Write-Host "  docker compose -f `"$composeFile`" ps"
Write-Host ""
Write-Host "Follow logs with:"
Write-Host "  docker compose -f `"$composeFile`" logs -f postgres"
