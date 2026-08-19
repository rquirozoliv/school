# courses-api

API REST para la gestión de **cursos** y **alumnos**, construida con Spring Boot y protegida con JWT.

## Nota sobre la versión de Java

El requerimiento original pedía Java 9. **Spring Boot 4.1.0 (la versión estable más reciente
a agosto de 2026, junto con Spring Framework 7.0.8) exige Java 17 como mínimo**, y Java 9 está
sin soporte desde 2018. Este proyecto usa **Java 21 (LTS)**, que permite aprovechar:

- `record` para DTOs inmutables (`CourseResponse`, `StudentCreateRequest`, etc.)
- Inferencia de tipos y expresiones lambda en toda la capa de mapeo y servicios
- `Optional` + lambdas para actualizaciones parciales (`PUT`) sin `if` anidados
- Text blocks, pattern matching y demás mejoras del lenguaje disponibles si el proyecto crece

## Stack técnico

| Capa            | Tecnología                                   |
|-----------------|-----------------------------------------------|
| Lenguaje        | Java 21                                       |
| Framework       | Spring Boot 4.1.0 / Spring Framework 7.0.8    |
| Web             | Spring MVC (API REST)                         |
| Persistencia    | Spring Data JPA + Hibernate                   |
| Base de datos   | MySQL 8 (instalada en la misma VM en GCP)     |
| Seguridad       | Spring Security + JWT (JJWT 0.13.0)           |
| Build           | Maven                                         |
| Boilerplate     | Lombok                                        |

## Arquitectura y buenas prácticas aplicadas

- **Patrón Repository**: `CourseRepository` / `StudentRepository` (Spring Data JPA).
- **Separación en capas** con responsabilidad única (SRP): `controller` → `service` → `repository`,
  con `mapper` dedicado a la conversión entidad ↔ DTO (nunca se expone la entidad JPA en la API).
- **DTOs inmutables** (`record`) separados para creación y actualización, evitando forzar
  validaciones de "obligatorio" en un `PUT` parcial.
- **Programación funcional**: los mappers exponen `Function<Entity, DTO>` reutilizables
  (`CourseMapper.TO_RESPONSE`, `StudentMapper.TO_RESPONSE`) usados con `Page::map` / `Stream::map`;
  las actualizaciones parciales usan `Optional` + method references en vez de condicionales anidados.
- **Validación declarativa** con Bean Validation (`@NotBlank`, `@Size`, `@Pattern`, `@Min`) más una
  anotación de dominio propia `@ValidRut` que verifica el dígito verificador del RUT chileno
  (algoritmo módulo 11, en `RutUtils`, cubierto por pruebas unitarias).
- **Manejo centralizado de errores** (`GlobalExceptionHandler`) que traduce cada excepción a un
  código HTTP y a un cuerpo `ApiError` consistente.
- **JWT stateless**: sin sesiones de servidor, un `OncePerRequestFilter` valida el token en cada
  petición y delega en el contrato de Spring Security (`SecurityFilterChain`).

## Endpoints

Todos, salvo `GET /token` y `GET /actuator/health`, requieren el header
`Authorization: Bearer <token>`.

### Autenticación

| Método | Ruta      | Descripción                                  |
|--------|-----------|-----------------------------------------------|
| GET    | `/token`  | Emite un JWT (sin parámetros ni credenciales) |

### Cursos

| Método | Ruta            | Éxito | Error                     |
|--------|-----------------|-------|---------------------------|
| GET    | `/courses`      | 200 (lista paginada, `?page=&size=&sort=`) | |
| GET    | `/courses/all`  | 200 (lista completa) | |
| GET    | `/courses/{id}` | 200   | 404 si no existe          |
| POST   | `/courses`      | 201   | 400 si el JSON es inválido |
| PUT    | `/courses/{id}` | 200   | 404 si no existe, 400 si inválido |
| DELETE | `/courses/{id}` | 200   | 404 si no existe          |

```json
// POST /courses
{ "name": "Matemáticas", "code": "MAT1" }
```

### Alumnos

Mismo patrón que cursos, en `/students`. `GET /students` acepta además `?courseId=` para filtrar.

```json
// POST /students
{
  "rut": "12.345.678-5",
  "name": "Ana",
  "lastname": "Pérez",
  "age": 22,
  "courseId": 1
}
```

## Cómo correr el proyecto localmente

### Opción A: Docker Compose (recomendado, levanta MySQL + la app)

```bash
docker compose up --build
```

La API queda disponible en `http://localhost:8080`.

### Opción B: MySQL local + Maven

1. Crea la base de datos (o deja que `createDatabaseIfNotExist=true` lo haga por ti):
   ```sql
   CREATE USER 'courses_user'@'localhost' IDENTIFIED BY 'courses_pass';
   CREATE DATABASE courses_db;
   GRANT ALL PRIVILEGES ON courses_db.* TO 'courses_user'@'localhost';
   ```
2. Ejecuta:
   ```bash
   mvn spring-boot:run
   ```

### Probar rápidamente con curl

```bash
TOKEN=$(curl -s http://localhost:8080/token | jq -r .accessToken)

curl -X POST http://localhost:8080/courses \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Matemáticas","code":"MAT1"}'
```

## Pruebas

```bash
mvn test
```

Incluye pruebas unitarias del validador de RUT y una prueba de integración (`MockMvc` + H2)
que verifica que `/token` es público, que `/courses` exige autenticación y que la validación
de payload responde 400.

## Despliegue en Google Cloud Platform

Ver [`DEPLOY.md`](./DEPLOY.md) para el paso a paso completo (capa gratuita de GCP + GitHub Actions).
