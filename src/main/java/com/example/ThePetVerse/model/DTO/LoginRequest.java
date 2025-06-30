package com.example.ThePetVerse.model.DTO;

import jakarta.validation.constraints.NotBlank;

// Define y valida los datos que se esperan cuando un usuario intenta iniciar sesión
// Es como el formulario de inicio de sesión
public class LoginRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio ♡") // Valida que no esté vacío
    private String username;

    @NotBlank(message = "La contraseña es obligatoria ♡") // Valida que no esté vacío
    private String password;

    // Constructor Vacio
    public LoginRequest() {
    }

    // Constructor para inicializar los campos
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Métodos set y get
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
