package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.DetalleVenta;
import com.andregarcia.kinalapp.service.IDetalleVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/detalles")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    // GET: Lista todos los detalles (útil para auditoría)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("detalles", detalleVentaService.listarTodos());
        return "ventas/lista-detalles";
    }

    // GET: Busca un detalle específico
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        detalleVentaService.buscarPorId(id).ifPresent(detalle -> model.addAttribute("detalle", detalle));
        return "ventas/ver-detalle";
    }

    // POST: Agrega un producto (detalle) a una venta existente
    @PostMapping
    public String guardar(@ModelAttribute DetalleVenta detalleVenta) {
        try {
            detalleVentaService.guardar(detalleVenta);
            // Tras agregar el detalle, redirigimos a la vista de la factura principal
            return "redirect:/ventas/" + detalleVenta.getVenta().getCodigoVenta();
        } catch (RuntimeException e) {
            return "redirect:/ventas?errorDetalle=true";
        }
    }

    /* *
     * INMUTABILIDAD FISCAL APLICADA
     * Los métodos actualizar (PUT) y eliminar (DELETE)
     * han sido removidos intencionalmente. Una vez que
     * un producto entra a la factura, no se puede alterar :)
     *
     */
}