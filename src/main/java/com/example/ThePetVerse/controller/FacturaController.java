package com.example.ThePetVerse.controller;


import com.example.ThePetVerse.model.Factura;
import com.example.ThePetVerse.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaService service;

    @PostMapping
    public Factura crear(@RequestBody Factura factura) {
        return service.crearFactura(factura);
    }

    @PostMapping("/{id}/agregar")
    public ResponseEntity<?> agregarProductos(@PathVariable Long id, @RequestBody Map<String, List<Long>> ids) {
        List<Long> idsProductos = ids.get("idsProductos");

        if (idsProductos == null || idsProductos.isEmpty()) {
            return ResponseEntity.badRequest().body("La lista 'idsProductos' no puede ser nula ni vacía.");
        }

        Factura factura = service.agregarProductos(id, idsProductos);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/{id}/total")
    public Map<String, Object> calcularTotal(@PathVariable Long id) {
        Factura f = service.obtenerPorId(id);
        double total = service.calcularTotal(id);
        return Map.of(
                "cliente", f.getCliente(),
                "productos", f.getProductos(),
                "total", total
        );
    }
    @DeleteMapping
    public ResponseEntity<Void> eliminarTodasLasFacturas() {
        service.deleteAll(); // o usa service
        return ResponseEntity.noContent().build();
    }



}
