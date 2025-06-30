package com.example.ThePetVerse.security;

import com.example.ThePetVerse.model.User;
import com.example.ThePetVerse.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Implementación personalizada de cómo la  aplicación carga la información de un usuario
@Service
public class UserDetailsServiceImpl implements UserDetailsService { // utiliza esta interfaz para cargar los detalles de un usuario durante el proceso de autenticación

    @Autowired
    UserRepository userRepository;

    //Spring Security llama este metodo cuando necesita obtener los detalles de un usuario basándose en su nombre de usuario (por ejemplo, durante el proceso de inicio de sesión o cuando se valida un JWT)
    @Override
    @Transactional //asegura que el metodo se ejecute dentro de una transacción de base de datos. Si ocurre un error durante la operación (por ejemplo, al buscar el usuario), la transacción se revertirá
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca un usuario en la base de datos por su nombre de usuario
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username));

        // Construye y devuelve un objeto UserDetailsImpl a partir del User encontrado
        return UserDetailsImpl.build(user);
    }
}
