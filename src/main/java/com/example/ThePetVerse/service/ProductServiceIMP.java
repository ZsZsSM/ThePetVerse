package com.example.ThePetVerse.service;


import com.example.ThePetVerse.model.Products;
import com.example.ThePetVerse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceIMP implements ProductService {


    @Autowired
    private ProductRepository repo;

    @Override
    public List<Products> getAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Products> getById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Products save(Products producto) {
        return repo.save(producto);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
    @Override
    public List<Products> searchByName(String nombre) {
        return repo.findByNombreContaining(nombre);
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}