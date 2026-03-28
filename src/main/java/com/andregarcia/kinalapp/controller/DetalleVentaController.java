package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.DetalleVenta;
import com.andregarcia.kinalapp.service.IDetalleVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalles")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listar() {
        return ResponseEntity.ok(detalleVentaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> buscarPorId(@PathVariable Long id) {
        return detalleVentaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta) {
        try {
            DetalleVenta nuevoDetalle = detalleVentaService.guardar(detalleVenta);
            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Atrapa errores de validación y si el producto no existe
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody DetalleVenta detalleVenta) {
        try {
            if (!detalleVentaService.existeId(id)) {
                return ResponseEntity.notFound().build();
            }
            DetalleVenta detalleActualizado = detalleVentaService.actualizar(id, detalleVenta);
            return ResponseEntity.ok(detalleActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            if (!detalleVentaService.existeId(id)) {
                return ResponseEntity.notFound().build();
            }
            detalleVentaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}