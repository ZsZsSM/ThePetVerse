package com.example.ThePetVerse.model.DTO;

// Estandariza la forma en que el servidor envía mensajes simples al cliente
public class MessageResponse {
    // En lugar de enviar un String simple directamente como respuesta HTTP
    // Esta clase lo envuelve en un objeto.
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
