package com.example.ThePetVerse.model;

import jakarta.persistence.*;

@Entity //Entidad para poder trabajarla en mysql
@Table(name="users",  //creamos la tabla users
        uniqueConstraints = { //le definimos que deben haber valores unicos para cada usuario que se registre
                @UniqueConstraint(columnNames = "username"), // un unico nombre de usuario
                @UniqueConstraint(columnNames = "email") }) // un unico email
public class User {
    @Id //LLave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Se autogenera solo el id
    @Column(name="id", nullable = false, unique = true, length = 10)
    private Integer id;
    @Column(name="username", nullable = false, length = 70)
    private String username;
    @Column(name="email", nullable = false, length = 150)
    private String email;
    @Column(name="password", nullable = false, length = 150)
    private String password;

    //constructor sin parámetros
    public User() {
    }

    //Constructor con parámetros
    public User( String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(Integer id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //Métodos set y get
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
