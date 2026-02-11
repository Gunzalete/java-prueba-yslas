#!/bin/bash

# Script de conveniencia para iniciar el proyecto con Docker
# Uso: ./start-docker.sh

set -e

echo "🐋 Iniciando Prueba Técnica Java con Docker..."
echo ""

# Verificar que Docker esté instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Error: Docker no está instalado"
    echo "Instala Docker desde: https://docs.docker.com/get-docker/"
    exit 1
fi

# Verificar que Docker Compose esté instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Error: Docker Compose no está instalado"
    echo "Instala Docker Compose desde: https://docs.docker.com/compose/install/"
    exit 1
fi

# Verificar que Docker esté corriendo
if ! docker info &> /dev/null; then
    echo "❌ Error: Docker daemon no está corriendo"
    echo "Inicia Docker Desktop o el servicio de Docker"
    exit 1
fi

echo "✅ Docker está instalado y corriendo"
echo ""

# Detener contenedores previos si existen
if docker ps -a | grep -q prueba-java-yslas; then
    echo "🛑 Deteniendo contenedores previos..."
    docker-compose down
    echo ""
fi

# Construir y levantar
echo "🔨 Construyendo imagen Docker (puede tomar 2-3 minutos)..."
echo ""
docker-compose up --build -d

echo ""
echo "⏳ Esperando que la aplicación inicie..."
echo ""

# Esperar a que la app esté lista (máximo 60 segundos)
for i in {1..60}; do
    if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo ""
        echo "✅ ¡Aplicación lista!"
        echo ""
        echo "📍 API disponible en: http://localhost:8080"
        echo ""
        echo "🧪 Prueba rápida:"
        echo "   curl http://localhost:8080/api/products"
        echo ""
        echo "📚 Ver ejemplos completos: EJEMPLOS_API.md"
        echo "📋 Ver logs: docker-compose logs -f"
        echo "🛑 Detener: docker-compose down"
        echo ""
        exit 0
    fi
    sleep 1
done

echo ""
echo "⚠️  La aplicación está tardando más de lo esperado"
echo "🔍 Ver logs con: docker-compose logs"
echo ""
exit 1
