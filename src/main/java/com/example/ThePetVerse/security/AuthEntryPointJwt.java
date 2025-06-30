package com.example.ThePetVerse.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Maneja qué sucede cuando un usuario intenta acceder a un recurso protegido sin estar autenticado o con credenciales inválidas
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint{
    // Registra mensajes de error cuando ocurra un problema de autenticación
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    // Es el punto de entrada para el manejo de excepciones de autenticación
    // Se invoca cuando una solicitud HTTP no autenticada intenta acceder a un recurso seguro
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException, ServletException {
            logger.error("Error de autorización: {}", authException.getMessage());
            // Envía una respuesta HTTP 401 Unauthorized cuando la autenticación falla
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: No autorizado");
        }
    }
