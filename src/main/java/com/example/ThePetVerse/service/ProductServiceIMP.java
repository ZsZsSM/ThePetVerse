package com.example.ThePetVerse.service;


import com.example.ThePetVerse.model.Productos;
import com.example.ThePetVerse.repository.PetRepository;
import com.example.ThePetVerse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceIMP implements ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Productos> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Productos> getById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Productos save(Productos product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Productos> searchByName(String nombre) {
        return productRepository.findByNombreContaining(nombre);

    }
}