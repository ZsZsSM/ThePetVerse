package com.example.ThePetVerse.controller;


import com.example.ThePetVerse.model.Products;
import com.example.ThePetVerse.service.FacturaService;
import com.example.ThePetVerse.service.ProductService;
import com.example.ThePetVerse.service.ProductServiceIMP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class MarketController {
    @Autowired
    private ProductService service;
    private FacturaService facturaService;

    @GetMapping
    public List<Products> listar() {
        return service.getAll();
    }

    @PostMapping
    public Products crear(@RequestBody Products producto) {
        return service.save(producto);
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarTodosLosProductos() {
        // Primero eliminar todas las referencias de productos en facturas
        facturaService.eliminarTodasLasReferenciasDeProductos();

        // Luego eliminar todos los productos
        service.deleteAll();

        return ResponseEntity.noContent().build();
    }
}
