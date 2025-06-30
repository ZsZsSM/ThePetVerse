package com.example.ThePetVerse.security;

import com.example.ThePetVerse.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Adaptador o envoltorio para la entidad User, permitiendo que Spring Security la entienda y la utilice para procesos de autenticación y autorización
public class UserDetailsImpl implements UserDetails {

    //Identificador de versión para la serialización
    private static final long serialVersionUID = 1L;

    //Estas propiedades almacenan la información básica de tu usuario
    private Integer id;
    private String username;
    private String email;

    //  Ignora esta propiedad cuando serialice el objeto a JSON.
    //  Esto previene que la contraseña del usuario (incluso encriptada) se envíe accidentalmente al cliente
    //  Lo cual es una excelente práctica de seguridad
    @JsonIgnore
    private String password;

    // Constructor con parámetros
    public UserDetailsImpl(Integer id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    // Metodo estatico build
    // Convierte la entidad User en un objeto UserDetailsImpl que Spring Security puede usar
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
    }
    // Este metodo es parte de la interfaz UserDetails y está destinado a devolver la lista de autoridades que tiene el usuario
    // En este caso no tenemos permisos ni roles entonces el metodo esta vacío, pero se incluye con la implementacion de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    //Devuelve la contraseña del usuario
    @Override
    public String getPassword() {
        return password;
    }

    //Devuelve el nombre de usuario
    @Override
    public String getUsername() {
        return username;
    }

    //Estos son métodos getter adicionales para acceder al ID y al correo electrónico del usuario
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // Estos métodos son parte de la interfaz UserDetails y están diseñados para proporcionar el estado de la cuenta del usuario a Spring Security
    // La cuenta del usuario nunca expira
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // La cuenta del usuario nunca se bloquea
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Las credenciales (contraseña) del usuario nunca expiran
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // La cuenta del usuario siempre está habilitada
    @Override
    public boolean isEnabled() {
        return true;
    }

    // equals y hashCode (para comparaciones de usuario)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
