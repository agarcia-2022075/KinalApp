package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Cliente;
import com.andregarcia.kinalapp.service.IClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // Cambiado de @RestController a @Controller 
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // GET: Muestra la vista con todos los clientes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/listar-clientes"; // Retorna el archivo HTML
    }

    // GET: Muestra la vista solo con clientes activos
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("clientes", clienteService.listarActivos());
        return "clientes/listar-clientes";
    }

    // GET: Busca un cliente por DPI y lo envía a la vista
    @GetMapping("/{dpi}")
    public String buscarPorId(@PathVariable String dpi, Model model) {
        clienteService.buscarPorDPI(dpi).ifPresent(cliente -> model.addAttribute("cliente", cliente));
        return "clientes/detalle-cliente";
    }

    // POST: Guarda un nuevo cliente desde un formulario HTML
    @PostMapping
    public String guardar(@ModelAttribute Cliente cliente) {
        try {
            clienteService.guardar(cliente);
            return "redirect:/clientes"; // Redirige a la lista tras guardar con éxito
        } catch (IllegalArgumentException e) {
            return "redirect:/clientes?error=true";
        }
    }

    // PUT: Actualiza un cliente desde un formulario
    @PutMapping("/{dpi}")
    public String actualizar(@PathVariable String dpi, @ModelAttribute Cliente cliente) {
        try {
            if (clienteService.existeDPI(dpi)) {
                clienteService.actualizar(dpi, cliente);
            }
            return "redirect:/clientes";
        } catch (Exception e) {
            return "redirect:/clientes?error=true";
        }
    }

    // DELETE: Elimina un cliente
    @DeleteMapping("/{dpi}")
    public String eliminar(@PathVariable String dpi) {
        try {
            if (clienteService.existeDPI(dpi)) {
                clienteService.eliminar(dpi);
            }
            return "redirect:/clientes";
        } catch (Exception e) {
            return "redirect:/clientes?error=true";
        }
    }
}