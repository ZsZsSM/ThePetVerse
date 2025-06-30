package com.example.ThePetVerse.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Factura {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String cliente;
        private LocalDate fecha;

        @ManyToMany
        @JoinTable(
                name = "factura_productos",
                joinColumns = @JoinColumn(name = "factura_id"),
                inverseJoinColumns = @JoinColumn(name = "productos_id")
        )
        private List<Products> productos = new ArrayList<>();

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getCliente() { return cliente; }
        public void setCliente(String cliente) { this.cliente = cliente; }

        public LocalDate getFecha() { return fecha; }
        public void setFecha(LocalDate fecha) { this.fecha = fecha; }

        public List<Products> getProductos() { return productos; }

        public void setProductos (List<Products> productos) { this.productos = productos; }
    }


