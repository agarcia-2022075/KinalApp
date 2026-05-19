package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Venta;
import com.andregarcia.kinalapp.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final IClienteService clienteService;
    private final IProductoService productoService;
    private final IUsuarioService usuarioService;
    private final IVentaService ventaService;

    public DashboardController(IClienteService clienteService, IProductoService productoService,
                               IUsuarioService usuarioService, IVentaService ventaService) {
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.ventaService = ventaService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Estadísticas básicas
        long totalClientes = clienteService.listarTodos().size();
        long totalProductos = productoService.listarTodos().size();
        long totalUsuarios = usuarioService.listarTodos().size();
        
        List<Venta> todasLasVentas = ventaService.listarTodos();
        long totalVentasCount = todasLasVentas.size();
        
        BigDecimal ingresosTotales = todasLasVentas.stream()
                .filter(v -> v.getEstado() == 1) // Solo ventas activas
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Últimas 5 ventas para la tabla de actividad reciente
        List<Venta> ultimasVentas = todasLasVentas.stream()
                .sorted((v1, v2) -> v2.getCodigoVenta().compareTo(v1.getCodigoVenta()))
                .limit(5)
                .collect(Collectors.toList());

        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("totalProductos", totalProductos);
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalVentas", totalVentasCount);
        model.addAttribute("ingresosTotales", ingresosTotales);
        model.addAttribute("ultimasVentas", ultimasVentas);

        return "dashboard";
    }

    @GetMapping("/403")
    public String accesoDenegado() {
        return "403";
    }
}
