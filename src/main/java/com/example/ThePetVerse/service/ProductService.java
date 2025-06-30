package com.example.ThePetVerse.service;

import com.example.ThePetVerse.model.Products;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Products> getAll();
    Optional<Products> getById(Long id);
    Products save(Products product);
    void delete(Long id);
    List<Products> searchByName(String nombre);
    void deleteAll();

}
