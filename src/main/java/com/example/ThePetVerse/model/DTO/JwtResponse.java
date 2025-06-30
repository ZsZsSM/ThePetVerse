package com.example.ThePetVerse.model.DTO;

// Paquete de información que el servidor le entrega al cliente
public class JwtResponse {
    //Esta es la pieza más importante
    // Aquí se guarda el JWT (Json Web Token) que el servidor ha generado para el usuario recién autenticado
    // Este token es lo que el cliente usará en futuras solicitudes para probar que está autenticado y tiene permiso para acceder a recursos protegidos
    private String token; // El token JWT real
    // Ttipo de token más común para los JWT
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
