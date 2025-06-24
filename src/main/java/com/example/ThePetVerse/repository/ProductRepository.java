package com.example.ThePetVerse.repository;

import com.example.ThePetVerse.model.Pet;
import com.example.ThePetVerse.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Productos, Long> {

    List<Productos> findByNombreContaining(String nombre);
}
