# Carmar Intranet Backend

Backend Spring Boot 3.5.4 (Java 21).

## Endpoint inicial
Lee órdenes desde un archivo Excel (XLSX), pestaña con el nombre del año (por ejemplo `2025`).

```
GET /api/orders?year=2025
GET /api/orders?year=2025&path=/ruta/a/archivo.xlsx
```

- Por defecto, la ruta se toma de `application.properties` en `carmar.excel.file`.
- Los nombres de columnas se normalizan (sin acentos, minúsculas, espacios a `_`). Cada fila se devuelve como JSON.

## Ejecutar
```bash
./mvnw spring-boot:run
```
Abrir: http://localhost:8080/swagger-ui/index.html (si deseás usar la UI de OpenAPI)
