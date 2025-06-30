package com.example.ThePetVerse.model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;
// Define y valida los datos que se esperan cuando un nuevo usuario intenta registrarse (crear una cuenta)
public class SignupRequest {
    @NotBlank (message = "El nombre de usuario es obligatorio ♡") // Valida que no esté vacío
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "El correo electrónico es obligatorio ♡") // Valida que no esté vacío
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank(message = "La contraseña es obligatoria ♡") // Valida que no esté vacío
    @Size(min = 6, max = 40)
    private String password;

    // Constructor vacío
    public SignupRequest() {
    }

    // Constructor con parámetros
    public SignupRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
