package com.example.ThePetVerse.controller;

// Importaciones de clases DTO, ahora apuntando a 'model.dto' (asegúrate de que estas rutas sean correctas)
import com.example.ThePetVerse.model.DTO.LoginRequest;
import com.example.ThePetVerse.model.DTO.SignupRequest;
import com.example.ThePetVerse.model.DTO.JwtResponse;
import com.example.ThePetVerse.model.DTO.MessageResponse;

// Resto de importaciones necesarias
import com.example.ThePetVerse.repository.UserRepository; // Necesitarás este
import com.example.ThePetVerse.model.User; // Asegúrate de que esta importación sea correcta
import com.example.ThePetVerse.security.JwtUtils;
import com.example.ThePetVerse.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Eliminamos las importaciones de Set, List, Collectors, HashSet, ERole, RoleRepository, Role

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository; // Necesitas este repositorio


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // ELIMINADO: No generamos lista de roles si no los usamos
        // List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        // Asegúrate que tu JwtResponse tenga un constructor que acepte:
        // (String jwt, Long id, String username, String email)
        // sin el campo 'roles' al final si no lo vas a usar.
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()
                /* , roles */ // ELIMINADO: No pasamos roles
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: ¡El nombre de usuario ya está en uso!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: ¡El correo electrónico ya está en uso!");
        }

        // Crear una nueva cuenta de usuario
        User user = new User(); // Asumiendo constructor vacío en la clase User
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword())); // Encripta la contraseña

        userRepository.save(user);

        return ResponseEntity.ok("¡Usuario registrado exitosamente!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        // Asumo que el ID de la entidad User es Long, por lo que convertimos Integer a Long.
        // Si tu ID de User es Integer, elimina Long.valueOf() y solo usa 'id'.
        Optional<User> userOp = userRepository.findById(Integer.valueOf(id));

        if (!userOp.isPresent()) {
            // Devuelve un String de error para coincidir con el formato deseado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con el ID " + id + " no se encuentra registrado ♡");
        }

        userRepository.deleteById(Integer.valueOf(id));
        // Devuelve un String de éxito para coincidir con el formato deseado
        return ResponseEntity.ok("Usuario con ID: " + id + " eliminado correctamente.");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@Valid @PathVariable Integer id, @RequestBody User userEdit, BindingResult result) {
        // 1. Manejo de errores de validación del DTO entrante
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // 2. Verificar si el ID en la ruta coincide con el ID en el cuerpo de la petición (si se proporciona)
        // Asumo que el ID de la entidad User es Long.
        if (userEdit.getId() != null && !userEdit.getId().equals(Integer.valueOf(id))) {
            // Devuelve un String de error para coincidir con el formato deseado
            return ResponseEntity.badRequest().body("Error: El ID de la busqueda  no coincide con el ID del usuario en el cuerpo.");
        }

        // 3. Buscar el usuario existente por el ID de la ruta
        Optional<User> userOp = userRepository.findById(Integer.valueOf(id)); // Usamos Long.valueOf(id) si el ID de User es Long

        if (!userOp.isPresent()) {
            // Devuelve un String de error para coincidir con el formato deseado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con el ID " + id + " no se encuentra registrado ♡");
        }

        User existingUser = userOp.get();

        // 4. Actualizar el nombre de usuario (si se proporciona y es diferente)
        if (userEdit.getUsername() != null && !userEdit.getUsername().isEmpty() && !existingUser.getUsername().equals(userEdit.getUsername())) {
            // Comprueba si el nuevo nombre de usuario ya existe, excluyendo al propio usuario
            if (userRepository.existsByUsernameAndIdNot(userEdit.getUsername(), existingUser.getId())) {
                return ResponseEntity.badRequest().body("Error: El nombre de usuario '" + userEdit.getUsername() + "' ya está en uso.");
            }
            existingUser.setUsername(userEdit.getUsername());
        }

        // 5. Actualizar el correo electrónico (si se proporciona y es diferente)
        if (userEdit.getEmail() != null && !userEdit.getEmail().isEmpty() && !existingUser.getEmail().equals(userEdit.getEmail())) {
            // Comprueba si el nuevo correo electrónico ya existe, excluyendo al propio usuario
            if (userRepository.existsByEmailAndIdNot(userEdit.getEmail(), existingUser.getId())) {
                return ResponseEntity.badRequest().body("Error: El correo electrónico '" + userEdit.getEmail() + " ya fue regsitrado.");
            }
            existingUser.setEmail(userEdit.getEmail());
        }

        // 6. Guardar el usuario actualizado
        userRepository.save(existingUser);

        // 7. Devolver un mensaje de éxito
        return ResponseEntity.ok("Usuario con ID: " + id + " editado correctamente.");
    }

    @GetMapping("/users") // Nuevo endpoint para obtener todos los usuarios
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll(); // Obtiene todos los usuarios del repositorio
        return ResponseEntity.ok(users); // Devuelve la lista de usuarios con un estado 200 OK
    }
}

