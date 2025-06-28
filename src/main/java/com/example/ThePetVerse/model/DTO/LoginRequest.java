package com.example.ThePetVerse.model.DTO;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio") // Valida que no esté vacío
    private String username;

    @NotBlank(message = "La contraseña es obligatoria") // Valida que no esté vacío
    private String password;

    // Constructor por defecto (puede ser necesario para algunos frameworks)
    public LoginRequest() {
    }

    // Constructor para inicializar los campos
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // métodos Get y Set

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
