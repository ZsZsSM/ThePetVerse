package com.example.ThePetVerse.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Guardián para las solicitudes entrantes, interceptándolas para buscar y validar un Token Web JSON (JWT)
// Si se encuentra un JWT válido, autentica al usuario para la solicitud actual
public class AuthTokenFilter extends OncePerRequestFilter { // Esta es una clase del framework Spring que asegura que el filtro se ejecute solo una vez por solicitud

    @Autowired
    private JwtUtils jwtUtils;// Genera, valida y extrae información (como el nombre de usuario) de los JWT

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Carga datos específicos del usuario

    // Se utiliza para registrar mensajes para depuración y monitoreo, especialmente cuando ocurren problemas de autenticación
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    // Metodo principal donde reside la lógica de filtrado
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
            try {
                String jwt = parseJwt(request);// Llama al metodo auxiliar parseJwt para extraer el JWT
                if (jwt != null && jwtUtils.validateJwtToken(jwt)) { //verifica si el token es válido
                    String username = jwtUtils.getUserNameFromJwtToken(jwt); //Si el JWT es válido, extrae el nombre de usuario
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);//Usando el nombre de usuario extraído, carga los detalles completos del usuario
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( //Crea un objeto de autenticación que Spring Security utiliza para representar a un usuario autenticado
                            userDetails, null, userDetails.getAuthorities());

                    // Agrega detalles adicionales sobre la solicitud web
                    // (como la dirección IP remota, el ID de sesión)al objeto de autenticación.
                    // Esto puede ser útil para fines de registro
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication); //Una vez que la autenticación se establece en el SecurityContext, Spring Security reconoce al usuario como autenticado para la solicitud actual
                }
                //Si ocurre algún error durante el análisis o la validación del JWT
                //Se captura la excepción y se registra un mensaje de error
            } catch (Exception e) {
                logger.error("No se pudo establecer la autenticación del usuario: {}", e.getMessage());
            }
            filterChain.doFilter(request, response); // Asegura que la solicitud continúe su procesamiento normal después de la verificación de autenticación
        }

        //Obtiene el valor del encabezado HTTP Authorization de la solicitud entrante
        //Comprueba si el encabezado existe, tiene texto y comienza con "Bearer "
        //Si el encabezado es válido, extrae la cadena JWT real eliminando el prefijo "Bearer "
        //Si el encabezado Authorization no está presente o no comienza con "Bearer ", devuelve null, lo que indica que no se encontró ningún JWT
        private String parseJwt(HttpServletRequest request) {
            String headerAuth = request.getHeader("Authorization");
            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                return headerAuth.substring(7, headerAuth.length());
            }
            return null;
        }
    }

