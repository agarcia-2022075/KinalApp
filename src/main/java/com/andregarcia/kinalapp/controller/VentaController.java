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

    // 1. Listar ventas (Historial)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaService.listarTodos());
        return "listar-ventas";
    }

    // 2. Mostrar formulario inicial (Cabecera)
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "formulario-venta";
    }

    // 3. Guardar cabecera y REDIRIGIR AL POS (Mejora UX)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Venta venta) {
        // Capturamos el objeto ya guardado (que ahora sí tiene un ID generado por MySQL)
        Venta ventaGuardada = ventaService.guardar(venta);

        // Redirigimos directamente al detalle de esa nueva factura para que el cajero empiece a cobrar
        return "redirect:/ventas/detalle/" + ventaGuardada.getCodigoVenta();
    }

    // 4. Punto de Venta (Detalle interactivo)
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

    // 5. Anular factura (Seguridad fiscal)
    @GetMapping("/anular/{id}")
    public String anularVenta(@PathVariable Long id) {
        try {
            ventaService.buscarPorId(id).ifPresent(venta -> {
                venta.setEstado(0); // 0 = ANULADA
                ventaService.actualizar(id, venta);
            });
            return "redirect:/ventas";
        } catch (Exception e) {
            return "redirect:/ventas?error=true";
        }
    }
}