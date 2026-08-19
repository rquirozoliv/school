package com.portfolio.coursesapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Habilita el boton "Authorize" en Swagger UI para pegar el JWT obtenido
 * en GET /token y probar el resto de los endpoints sin salir del navegador.
 * Swagger UI queda disponible en /swagger-ui.html una vez levantada la app.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Courses API",
                version = "1.0.0",
                description = "API REST para gestion de cursos y alumnos, protegida con JWT."
        ),
        security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
