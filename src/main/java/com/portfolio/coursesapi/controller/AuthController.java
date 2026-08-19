package com.portfolio.coursesapi.controller;

import com.portfolio.coursesapi.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * GET /token: emite un JWT sin recibir parametros ni credenciales,
     * segun el lineamiento entregado. Nota de seguridad: en un escenario
     * real este endpoint deberia exigir credenciales de cliente (API key,
     * client_credentials de OAuth2, etc.); aqui queda abierto porque asi
     * fue especificado el contrato.
     */
    @GetMapping("/token")
    public ResponseEntity<TokenResponse> getToken() {
        String token = jwtTokenProvider.generateToken();
        long expiresInSeconds = jwtTokenProvider.getExpirationMs() / 1000;
        return ResponseEntity.ok(new TokenResponse(token, "Bearer", expiresInSeconds));
    }

    public record TokenResponse(String accessToken, String tokenType, long expiresInSeconds) {
    }
}
