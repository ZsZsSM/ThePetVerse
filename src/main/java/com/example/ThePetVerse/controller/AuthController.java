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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// Eliminamos las importaciones de Set, List, Collectors, HashSet, ERole, RoleRepository, Role

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository; // Necesitas este repositorio

    // @Autowired RoleRepository roleRepository; // ELIMINADO: No estamos usando roles

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
                    .body(new MessageResponse("Error: ¡El nombre de usuario ya está en uso!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: ¡El correo electrónico ya está en uso!"));
        }

        // Crear una nueva cuenta de usuario
        User user = new User(); // Asumiendo constructor vacío en la clase User
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword())); // Encripta la contraseña

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("¡Usuario registrado exitosamente!"));
    }
}

