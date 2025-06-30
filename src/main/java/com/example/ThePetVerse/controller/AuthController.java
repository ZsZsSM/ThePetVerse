package com.example.ThePetVerse.controller;

// Importaciones de clases DTO para datos que se envían y reciben en las peticiones y respuestas
import com.example.ThePetVerse.model.DTO.LoginRequest;
import com.example.ThePetVerse.model.DTO.SignupRequest;
import com.example.ThePetVerse.model.DTO.JwtResponse;

// Importaciones necesarias
import com.example.ThePetVerse.repository.UserRepository;
import com.example.ThePetVerse.model.User;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager; // Gestiona el proceso de autenticación de usuarios.

    @Autowired
    UserRepository userRepository; // Sirve como una capa de abstracción entre la lógica y la base de datos


    @Autowired
    PasswordEncoder encoder; // Encripta las contraseñas de los usuarios, lo cual es crucial para la seguridad

    @Autowired
    JwtUtils jwtUtils; // Genera y valida los tokens JWT

    //Toma el nombre de usuario y la contraseña del LoginRequest
    //Usa el AuthenticationManager para verificar las credenciales
    //Si las credenciales son válidas, genera un JWT (Json Web Token).
    //Devuelve el JWT junto con la información del usuario en un objeto JwtResponse.
    //Este token será usado por el cliente para acceder a recursos protegidos de la página.

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()
        ));
    }

    //Toma el nombre de usuario, correo electrónico y contraseña del SignupRequest
    //Verifica si el nombre de usuario o el correo electrónico ya existen en la base de datos para evitar duplicados
    //Si no existen, crea un nuevo objeto User, encripta la contraseña y lo guarda en la base de datos.
    //Devuelve un mensaje de éxito.

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: ¡El nombre de usuario ya está en uso♡!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: ¡El correo electrónico ya está en uso♡!");
        }

        // Crea una nueva cuenta de usuario
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword())); // Encripta la contraseña

        userRepository.save(user);
        return ResponseEntity.ok("¡Usuario registrado exitosamente♡!");
    }

    //Toma el id del usuario
    //Busca el usuario en la base de datos
    //Si lo encuentra, lo elimina
    //Devuelve un mensaje de éxito o un error si el usuario no fue encontrado

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        Optional<User> userOp = userRepository.findById(Integer.valueOf(id));
        if (!userOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con el ID " + id + " no se encuentra registrado ♡");
        }

        userRepository.deleteById(Integer.valueOf(id));
        return ResponseEntity.ok("Usuario con ID: " + id + " eliminado correctamente ♡");
    }

    //Toma el id del usuario y los nuevos datos del usuario del cuerpo de la petición
    //Realiza validaciones para asegurar que los datos son correctos y que el ID de la busqueda coincide con el ID en el cuerpo de la petición
    //Busca el usuario existente en la base de datos.
    //Si lo encuentra, actualiza su nombre de usuario y/o correo electrónico, asegurándose de que los nuevos valores no entren en conflicto con usuarios existentes
    //Guarda los cambios en la base de datos.
    //Devuelve un mensaje de éxito o un error si el usuario no fue encontrado o si hay problemas de validación

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
        // 2. Verifica si el ID en la ruta coincide con el ID en el cuerpo de la petición
        if (userEdit.getId() != null && !userEdit.getId().equals(Integer.valueOf(id))) {
            return ResponseEntity.badRequest().body("Error: El ID de la busqueda  no coincide con el ID del usuario en el cuerpo ♡");
        }
        // 3. Busca el usuario existente por el ID de la ruta
        Optional<User> userOp = userRepository.findById(Integer.valueOf(id));
        if (!userOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con el ID " + id + " no se encuentra registrado ♡");
        }
        User existingUser = userOp.get();
        // 4. Actualiza el nombre de usuario
        if (userEdit.getUsername() != null && !userEdit.getUsername().isEmpty() && !existingUser.getUsername().equals(userEdit.getUsername())) {
            // Comprueba si el nuevo nombre de usuario ya existe
            if (userRepository.existsByUsernameAndIdNot(userEdit.getUsername(), existingUser.getId())) {
                return ResponseEntity.badRequest().body("Error: El nombre de usuario '" + userEdit.getUsername() + "ya está en uso ♡");
            }
            existingUser.setUsername(userEdit.getUsername());
        }
        // 5. Actualiza el correo electrónico
        if (userEdit.getEmail() != null && !userEdit.getEmail().isEmpty() && !existingUser.getEmail().equals(userEdit.getEmail())) {
            // Comprueba si el nuevo correo electrónico ya existe
            if (userRepository.existsByEmailAndIdNot(userEdit.getEmail(), existingUser.getId())) {
                return ResponseEntity.badRequest().body("Error: El correo electrónico '" + userEdit.getEmail() + "ya fue regsitrado ♡");
            }
            existingUser.setEmail(userEdit.getEmail());
        }
        // 6. Guarda el usuario actualizado
        userRepository.save(existingUser);
        // 7. Devuelve un mensaje de éxito
        return ResponseEntity.ok("Usuario con ID: " + id + " editado correctamente ♡");
    }

    //Consulta la base de datos para obtener todos los usuarios
    //Devuelve la lista completa de usuarios

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll(); // Obtiene todos los usuarios del repositorio
        return ResponseEntity.ok(users); // Devuelve la lista de usuarios con un estado 200 OK
    }
}

