package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Producto;
import com.andregarcia.kinalapp.service.IProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // Cambiado para que Spring sepa que este controlador devuelve vistas Thymeleaf
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    // Mantenemos la inyección de dependencias por constructor
    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    // GET: Envía la lista completa del inventario a la vista HTML
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "productos/listar-productos";
    }

    // GET: Envía solo los productos activos (útil para el catálogo de ventas)
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("productos", productoService.listarActivos());
        return "productos/listar-productos";
    }

    // GET: Busca un producto por su ID para mostrar su detalle
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        productoService.buscarPorId(id).ifPresent(producto -> model.addAttribute("producto", producto));
        return "productos/detalle-producto";
    }

    // POST: Recibe el formulario HTML para crear un nuevo producto
    @PostMapping
    public String guardar(@ModelAttribute Producto producto) {
        try {
            productoService.guardar(producto);
            return "redirect:/productos"; // Refresca la lista visualmente
        } catch (IllegalArgumentException e) {
            return "redirect:/productos?error=true";
        }
    }

    // PUT: Guarda los cambios de un producto editado en el formulario
    @PutMapping("/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Producto producto) {
        try {
            if (productoService.existeId(id)) {
                productoService.actualizar(id, producto);
            }
            return "redirect:/productos";
        } catch (Exception e) {
            return "redirect:/productos?error=true";
        }
    }

    // DELETE: Elimina un producto del catálogo
    @DeleteMapping("/{id}")
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