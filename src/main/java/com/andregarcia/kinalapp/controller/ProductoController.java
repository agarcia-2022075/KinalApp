// Ubicación: C:\Spring_2022075\kinalapp\src\main\java\com\andregarcia\kinalapp\controller\ProductoController.java

package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Producto;
import com.andregarcia.kinalapp.service.IProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    // 1. Mostrar todos los productos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "listar-productos";
    }

    // 2. Mostrar solo activos (para el catálogo/ventas)
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("productos", productoService.listarActivos());
        return "listar-productos";
    }

    // 3. Mostrar el formulario vacío para crear un producto
    @GetMapping("/nuevo")
    public String mostrarFormularioDeCrear(Model model) {
        model.addAttribute("producto", new Producto());
        return "formulario-producto";
    }

    // 4. Mostrar el formulario lleno para editar un producto
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditar(@PathVariable Long id, Model model) {
        productoService.buscarPorId(id).ifPresent(producto -> model.addAttribute("producto", producto));
        return "formulario-producto";
    }

    /// 5. Guardar o Actualizar
    @PostMapping
    public String guardarOActualizar(@ModelAttribute Producto producto) {
        try {
            // JPA es inteligente: Si codigoProducto es 0 o nulo hace INSERT, si ya existe hace UPDATE
            productoService.guardar(producto);
            return "redirect:/productos";
        } catch (Exception e) {
            return "redirect:/productos?error=true";
        }
    }

    // 6. Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            if (productoService.existeId(id)) {
                productoService.eliminar(id);
            }
            return "redirect:/productos";
        } catch (Exception e) {
            return "redirect:/productos?error=true";
        }
    }
}