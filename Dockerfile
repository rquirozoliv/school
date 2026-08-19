# ---------------------------------------------------------------------------
# Etapa de build
# ---------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Se copia primero el pom.xml para cachear las dependencias en capas Docker
COPY pom.xml .
RUN mvn -q dependency:go-offline

COPY src src
RUN mvn -q clean package -DskipTests

# ---------------------------------------------------------------------------
# Etapa de runtime (imagen final, liviana)
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/courses-api.jar app.jar

EXPOSE 8080

# -Xmx acotado pensando en entornos con poca memoria (por ejemplo, para
# pruebas locales que simulan las condiciones de la VM e2-micro de GCP).
ENTRYPOINT ["java", "-Xmx256m", "-XX:+UseSerialGC", "-jar", "app.jar"]
