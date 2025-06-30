package com.example.ThePetVerse.repository;

import com.example.ThePetVerse.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Products, Long> {

    List<Products> findByNombreContaining(String nombre);
}
