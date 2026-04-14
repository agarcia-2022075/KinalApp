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
        // CORRECCIÓN: Nombre exacto según tu captura de pantalla
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
            // CORRECCIÓN CRÍTICA: Aquí faltaba cargar los clientes para el punto-de-venta.html
            model.addAttribute("clientes", clienteService.listarTodos());
        });
        return "punto-de-venta";
    }
}