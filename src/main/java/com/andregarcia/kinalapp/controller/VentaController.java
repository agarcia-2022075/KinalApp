package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Venta;
import com.andregarcia.kinalapp.service.IVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    // GET: Muestra el historial completo de ventas
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaService.listarTodos());
        return "ventas/historial-ventas";
    }

    // GET: Muestra solo ventas activas
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("ventas", ventaService.listarActivos());
        return "ventas/historial-ventas";
    }

    // GET: Busca una factura específica para verla (Solo Lectura)
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        ventaService.buscarPorId(id).ifPresent(venta -> model.addAttribute("venta", venta));
        return "ventas/detalle-factura";
    }

    // POST: Crea la cabecera de la factura (nueva venta)
    @PostMapping
    public String guardar(@ModelAttribute Venta venta) {
        try {
            ventaService.guardar(venta);
            return "redirect:/ventas";
        } catch (IllegalArgumentException e) {
            return "redirect:/ventas?error=true";
        }
    }

    /* *
     * INMUTABILIDAD FISCAL APLICADA
     * Los métodos actualizar (PUT) y eliminar (DELETE)
     * han sido removidos intencionalmente para evitar
     * la alteración de facturas emitidas :)
     *
     */
}