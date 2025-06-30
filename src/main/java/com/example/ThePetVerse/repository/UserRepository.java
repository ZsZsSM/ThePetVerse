package com.example.ThePetVerse.repository;

import com.example.ThePetVerse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    // Este metodo busa un usuario por su username
    Optional<User> findByUsername(String username);
    // Verifica si un username ya existe en la base de datos
    Boolean existsByUsername(String username);
    // Verifica si un email ya existe en la base de datos
    Boolean existsByEmail(String email);

    Boolean existsByUsernameAndIdNot(String username, Integer id); //

    Boolean existsByEmailAndIdNot(String email, Integer id); //

}
