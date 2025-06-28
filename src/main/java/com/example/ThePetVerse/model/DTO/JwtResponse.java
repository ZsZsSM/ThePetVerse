package com.example.ThePetVerse.model.DTO;

public class JwtResponse {
    private String token; // El token JWT real
    private String type = "Bearer"; // Tipo de token, por convención "Bearer"
    private Integer id; // ID del usuario
    private String username; // Nombre de usuario
    private String email; // Email del usuario

    // Constructor para construir la respuesta
    public JwtResponse(String accessToken, Integer id, String username, String email) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Getters y Setters
    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}
