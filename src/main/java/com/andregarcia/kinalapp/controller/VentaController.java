package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Venta;
import com.andregarcia.kinalapp.entity.DetalleVenta;
import com.andregarcia.kinalapp.service.IVentaService;
import com.andregarcia.kinalapp.service.IProductoService;
import com.andregarcia.kinalapp.service.IClienteService;
import com.andregarcia.kinalapp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;
    private final IProductoService productoService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public VentaController(IVentaService ventaService, IProductoService productoService,
                           IClienteService clienteService, IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.productoService = productoService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaService.listarTodos());
        return "listar-ventas";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "formulario-venta";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta) {
        ventaService.guardar(venta);
        return "redirect:/ventas";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        ventaService.buscarPorId(id).ifPresent(v -> {
            model.addAttribute("venta", v);
            model.addAttribute("nuevoDetalle", new DetalleVenta());
            model.addAttribute("productos", productoService.listarActivos());
            model.addAttribute("clientes", clienteService.listarTodos());
        });
        return "punto-de-venta";
    }

    // NUEVO MÉTODO: Anular factura cambiando su estado a 0
    @GetMapping("/anular/{id}")
    public String anularVenta(@PathVariable Long id) {
        try {
            ventaService.buscarPorId(id).ifPresent(venta -> {
                venta.setEstado(0); // 0 = ANULADA
                // Usamos actualizar para que se guarde el cambio de estado respetando la fecha y total
                ventaService.actualizar(id, venta);
            });
            return "redirect:/ventas";
        } catch (Exception e) {
            return "redirect:/ventas?error=true";
        }
    }
}