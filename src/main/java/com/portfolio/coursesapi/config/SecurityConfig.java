package com.portfolio.coursesapi.config;

import com.portfolio.coursesapi.security.JwtAuthenticationEntryPoint;
import com.portfolio.coursesapi.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.h2.server.web.JakartaWebServlet;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Toda la API es stateless y protegida con JWT, salvo GET /token
 * (emision de token, ver AuthController) y el health check de actuator
 * que GCP puede usar para monitoreo.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/h2-console", "/h2-console/**", "/swagger-ui/index.html", "/swagger-ui/index.html/**", "/error");
    }

    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2ConsoleServletRegistration() {
        // Usamos 'JakartaWebServlet' en lugar de 'WebServlet' para compatibilidad con Spring Boot 3
        ServletRegistrationBean<JakartaWebServlet> registration =
                new ServletRegistrationBean<>(new JakartaWebServlet());

        registration.addUrlMappings("/h2-console/*");
        registration.addUrlMappings("/swagger-ui/index.html/*");

        return registration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                // 1. Desactivar CSRF por completo (ya lo tenías al inicio, elimina el segundo .csrf)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Permitir acceso libre a las rutas públicas (incluyendo H2)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/token").permitAll()
                        .requestMatchers("/h2-console", "/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui/index.html", "/swagger-ui/index.html/**").permitAll()
                        .requestMatchers("/error").permitAll() // Añadido aquí también por seguridad
                        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 3. Permitir marcos (iframes) del mismo origen para la interfaz de H2
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                // 4. Control de excepciones y sesión de estado único
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5. Filtro JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
