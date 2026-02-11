@echo off
REM Script de conveniencia para Windows
REM Uso: start-docker.bat

echo.
echo 🐋 Iniciando Prueba Técnica Java con Docker...
echo.

REM Verificar que Docker esté instalado
docker --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Error: Docker no está instalado
    echo Instala Docker Desktop desde: https://docs.docker.com/get-docker/
    pause
    exit /b 1
)

REM Verificar que Docker Compose esté instalado
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Error: Docker Compose no está instalado
    pause
    exit /b 1
)

echo ✅ Docker está instalado
echo.

REM Detener contenedores previos
echo 🛑 Deteniendo contenedores previos (si existen)...
docker-compose down 2>nul
echo.

REM Construir y levantar
echo 🔨 Construyendo imagen Docker (puede tomar 2-3 minutos)...
echo.
docker-compose up --build -d

if errorlevel 1 (
    echo.
    echo ❌ Error al construir/levantar el contenedor
    echo 🔍 Ver logs con: docker-compose logs
    pause
    exit /b 1
)

echo.
echo ⏳ Esperando que la aplicación inicie...
echo.

REM Esperar a que la app esté lista
timeout /t 5 /nobreak >nul

REM Verificar health
curl -s http://localhost:8080/actuator/health >nul 2>&1
if errorlevel 1 (
    echo ⚠️  La aplicación está iniciando...
    echo 🔍 Ver progreso: docker-compose logs -f
    echo.
) else (
    echo ✅ ¡Aplicación lista!
    echo.
)

echo 📍 API disponible en: http://localhost:8080
echo.
echo 🧪 Prueba rápida:
echo    curl http://localhost:8080/api/products
echo.
echo 📚 Ver ejemplos: EJEMPLOS_API.md
echo 📋 Ver logs: docker-compose logs -f
echo 🛑 Detener: docker-compose down
echo.

pause
