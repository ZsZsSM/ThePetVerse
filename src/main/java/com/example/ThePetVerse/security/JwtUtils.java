package com.example.ThePetVerse.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component; //

import java.security.Key;
import java.util.Date;

//Maneja todas las operaciones relacionadas con los Tokens Web JSON (JWT): su generación, extracción de información y validación
@Component
public class JwtUtils {
    // Se utiliza para registrar mensajes para depuración y monitoreo, especialmente cuando ocurren problemas de autenticación
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Valores para la clave secreta y la expiración del token (se inyectan desde application.properties)
    @Value("${thepetverse.app.jwtSecret}") // Es la clave secreta que se usará para firmar y verificar los JWT, es crucial que esta clave sea fuerte y se mantenga en secreto en el servidor
    private String jwtSecret;

    @Value("${thepetverse.app.jwtExpirationMs}")
    private int jwtExpirationMs; //Es el tiempo de expiración del token en milisegundos. Define cuánto tiempo será válido un JWT desde el momento de su emisión.

    // Metodo para generar el JWT
    // Responsable de crear y firmar un nuevo JWT después de que un usuario ha iniciado sesión exitosamente
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) // Sujeto del token: el nombre de usuario
                .setIssuedAt(new Date()) // Fecha de emisión del token
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Fecha de expiración
                .signWith(key(), SignatureAlgorithm.HS512) // Firma el token con la clave secreta y algoritmo
                .compact(); // Construye el token JWT
    }

    // Metodo para obtener la clave de firma (Key)
    // Decodifica la clave secreta
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Metodo para obtener el nombre de usuario del token JWT
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Metodo para validar el token JWT
    // Crucial para la seguridad, ya que verifica si un JWT es válido y confiable.
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("La cadena JWT está vacía: {}", e.getMessage());
        }
        return false;
    }
}
