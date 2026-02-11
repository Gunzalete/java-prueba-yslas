#!/bin/bash
# Script quick para verificar que todo está listo

echo "📋 ESTADO DEL PROYECTO"
echo "=================================="
echo ""
echo "📄 Archivos principales:"
ls -lh README.md 2>/dev/null && echo "  ✅ README.md" || echo "  ❌ README.md NOT FOUND"
ls -lh pom.xml 2>/dev/null && echo "  ✅ pom.xml" || echo "  ❌ pom.xml NOT FOUND"
ls -lh docker-compose.yml 2>/dev/null && echo "  ✅ docker-compose.yml" || echo "  ❌ docker-compose.yml NOT FOUND"
echo ""

echo "📦 Colecciones Postman:"
ls -lh Postman_Collection.json 2>/dev/null && echo "  ✅ Postman_Collection.json" || echo "  ❌ Postman_Collection.json NOT FOUND"
ls -lh Postman_Environment.json 2>/dev/null && echo "  ✅ Postman_Environment.json" || echo "  ❌ Postman_Environment.json NOT FOUND"
echo ""

echo "📝 Archivos .md en el proyecto:"
md_count=$(ls -1 *.md 2>/dev/null | wc -l)
echo "  Total: $md_count archivo(s)"
ls -1 *.md 2>/dev/null
echo ""

echo "=================================="
echo "Para limpiar y dejar solo README.md:"
echo "  bash cleanup.sh"
echo "=================================="
