package com.example.ThePetVerse.controller;

import com.example.ThePetVerse.model.User; // Importa tu entidad User
import com.example.ThePetVerse.model.DTO.JwtResponse; // Importa tu DTO JwtResponse
import com.example.ThePetVerse.model.DTO.LoginRequest; // Importa tu DTO LoginRequest
import com.example.ThePetVerse.repository.UserRepository; // Importa tu UserRepository
import com.example.ThePetVerse.security.JwtUtils; // Importa tu JwtUtils
import com.example.ThePetVerse.security.UserDetailsImpl; // Importa UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired; // Para inyección de dependencias
import org.springframework.http.ResponseEntity; // Para construir respuestas HTTP
import org.springframework.security.authentication.AuthenticationManager; // Para manejar la autenticación
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Token de autenticación
import org.springframework.security.core.Authentication; // Objeto de autenticación de Spring Security
import org.springframework.security.core.context.SecurityContextHolder; // Para establecer el contexto de seguridad
import org.springframework.security.crypto.password.PasswordEncoder; // Para encriptar contraseñas
import org.springframework.web.bind.annotation.*; // Anotaciones REST

import jakarta.validation.Valid; // Para la validación de DTOs

@CrossOrigin(origins = "*", maxAge = 3600) // Permite peticiones CORS desde cualquier origen
@RestController // Marca esta clase como un controlador REST
@RequestMapping("/api/auth") // Mapea todas las peticiones a esta URL base
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager; // Inyecta el AuthenticationManager

    @Autowired
    UserRepository userRepository; // Inyecta el UserRepository

    @Autowired
    PasswordEncoder encoder; // Inyecta el PasswordEncoder

    @Autowired
    JwtUtils jwtUtils; // Inyecta el JwtUtils

    @PostMapping("/signin") // Mapea las peticiones POST a /api/auth/signin
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // 1. Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Establecer la autenticación en el contexto de seguridad de Spring
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar el token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 4. Obtener los detalles del usuario autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 5. Devolver la respuesta JWT (con el token, nombre de usuario, etc.)
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()));
    }

    // Este metodo es un placeholder para el registro de usuarios.
    // Aún no lo implementaremos completamente, pero lo dejamos aquí.
    @PostMapping("/signup") // Mapea las peticiones POST a /api/auth/signup
    public ResponseEntity<?> registerUser(@Valid @RequestBody User signupRequest) {
        // En una implementación real, aquí deberías:
        // 1. Verificar si el username/email ya existe.
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: ¡El nombre de usuario ya está en uso!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: ¡El correo electrónico ya está en uso!");
        }

        // 2. Crear un nuevo objeto User.
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword())); // ¡Importante: encriptar la contraseña!

        // 3. Guardar el usuario en la base de datos.
        userRepository.save(user);

        return ResponseEntity.ok("Usuario registrado exitosamente!");
    }
}
