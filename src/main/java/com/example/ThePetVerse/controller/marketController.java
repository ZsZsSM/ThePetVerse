package com.example.ThePetVerse.controller;


import com.example.ThePetVerse.model.Productos;
import com.example.ThePetVerse.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class marketController {

    @Autowired
    private ProductService service;

    @GetMapping
    public List<Productos> getAllProducts() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Productos> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Productos create(@RequestBody Productos product) {
        return service.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Productos> update(@PathVariable Long id, @RequestBody Productos newProduct) {
        return service.getById(id)
                .map(product -> {
                    product.setNombre(newProduct.getNombre());
                    product.setPrecio(newProduct.getPrecio());
                    product.setStock(newProduct.getStock());
                    return ResponseEntity.ok(service.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<Productos> search(@RequestParam String nombre) {
        return service.searchByName(nombre);
    }

}
