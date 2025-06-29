package com.example.ThePetVerse.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean; // Para definir beans de Spring
import org.springframework.context.annotation.Configuration; // Marca esta clase como una configuración de Spring
import org.springframework.security.authentication.AuthenticationManager; // Para manejar la autenticación
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Proveedor de autenticación
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Configuración para el AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Para seguridad a nivel de metodo (ej. @PreAuthorize)
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Para configurar la seguridad web
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Habilita la seguridad web de Spring
import org.springframework.security.config.http.SessionCreationPolicy; // Para manejar la política de sesión (stateless)
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Para codificar contraseñas
import org.springframework.security.crypto.password.PasswordEncoder; // Interfaz para codificador de contraseñas
import org.springframework.security.web.SecurityFilterChain; // Para construir la cadena de filtros de seguridad
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filtro para el AuthTokenFilter
@Configuration // Marca esta clase como una clase de configuración de Spring
@EnableWebSecurity // Habilita las características de seguridad web de Spring
@EnableMethodSecurity // Habilita la seguridad a nivel de metodo (ej. @PreAuthorize)
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService; // Nuestro servicio para cargar detalles de usuario

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler; // Manejador para errores de autenticación (401 Unauthorized)

    @Bean // Marca este metodo como un bean de Spring
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(); // Devuelve una instancia de nuestro filtro JWT
    }

    @Bean // Define un codificador de contraseñas (BCrypt)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usamos BCrypt para encriptar contraseñas
    }

    @Bean // Configura el proveedor de autenticación (DAO)
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService); // Nuestro servicio de detalles de usuario
        authProvider.setPasswordEncoder(passwordEncoder()); // Nuestro codificador de contraseñas

        return authProvider;
    }

    @Bean // Obtiene el AuthenticationManager de la configuración de Spring
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean // Configura la cadena de filtros de seguridad HTTP
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Deshabilita CSRF (importante para APIs REST stateless)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Maneja errores de autenticación
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura sesiones como stateless (sin estado)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll() // Permite acceso público a rutas de autenticación
                                .requestMatchers("/api/test/**").permitAll() // Permite acceso público a rutas de prueba (si las tuvieras)
                                .anyRequest().authenticated() // Cualquier otra petición requiere autenticación
                );

        // Agrega nuestro proveedor de autenticación
        http.authenticationProvider(authenticationProvider());

        // Agrega nuestro filtro JWT antes del filtro de autenticación de usuario/contraseña de Spring
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Construye y devuelve la cadena de filtros de seguridad
    }
}
