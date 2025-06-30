package com.example.ThePetVerse.repository;

import com.example.ThePetVerse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Puente principal para el código interactúe con la base de datos y realice operaciones sobre la información de los usuarios
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    // Este metodo busa un usuario por su username
    Optional<User> findByUsername(String username);
    // Verifica si un username ya existe en la base de datos
    Boolean existsByUsername(String username);
    // Verifica si un email ya existe en la base de datos
    Boolean existsByEmail(String email);
    // Cuando editas un usuario.
    // Verifica si un nombre de usuario ya existe en la base de datos
    // Pero ignora al usuario que estamos editando
    // Esto permite que un usuario cambie su nombre de usuario a otro que no esté en uso, o que conserve su propio nombre de usuario si no lo está cambiando
    Boolean existsByUsernameAndIdNot(String username, Integer id); //
    // Verifica si un email ya está registrado, excluyendo al usuario actual
    Boolean existsByEmailAndIdNot(String email, Integer id); //
}
