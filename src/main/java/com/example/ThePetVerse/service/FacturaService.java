package com.example.ThePetVerse.service;

import com.example.ThePetVerse.model.Factura;
import com.example.ThePetVerse.model.Products;
import com.example.ThePetVerse.repository.FacturaRepository;
import com.example.ThePetVerse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaService {

        @Autowired
        private FacturaRepository facturaRepo;

        @Autowired
        private ProductRepository productoRepo;

        public Factura crearFactura(Factura factura) {
            return facturaRepo.save(factura);
        }

    public Factura agregarProductos(Long facturaId, List<Long> idsProductos) {
        Factura factura = facturaRepo.findById(facturaId).orElseThrow();
        List<Products> productos = productoRepo.findAllById(idsProductos);
        productos.removeIf(p -> p == null); // Evita nulos
        factura.getProductos().addAll(productos);
        return facturaRepo.save(factura);
    }

        public double calcularTotal(Long facturaId) {
            Factura factura = facturaRepo.findById(facturaId).orElseThrow();
            return factura.getProductos().stream()
                    .mapToDouble(Products::getPrecio)
                    .sum();
        }
    public Factura eliminarProducto(Long facturaId, Long productoId) {
        Factura factura = facturaRepo.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + facturaId));

        Products producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        if (factura.getProductos().contains(producto)) {
            factura.getProductos().remove(producto);
            return facturaRepo.save(factura);
        } else {
            throw new IllegalArgumentException("Producto no está en la factura");
        }
    }

        public Factura obtenerPorId(Long id) {
            return facturaRepo.findById(id).orElseThrow();
        }

    public void deleteAll() {
        facturaRepo.deleteAll();
    }
}
