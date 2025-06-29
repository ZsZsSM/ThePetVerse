package com.example.ThePetVerse.service;

import com.example.ThePetVerse.model.Productos;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Productos> getAll();
    Optional<Productos> getById(Long id);
    Productos save(Productos product);
    void delete(Long id);
    List<Productos> searchByName(String nombre);
}
