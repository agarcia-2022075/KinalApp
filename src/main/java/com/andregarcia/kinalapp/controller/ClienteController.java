package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Cliente;
import com.andregarcia.kinalapp.service.IClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // 1. Mostrar todos los clientes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "listar-clientes";
    }

    // 2. Mostrar solo activos
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("clientes", clienteService.listarActivos());
        return "listar-clientes";
    }

    // 3. Mostrar el formulario vacío para crear
    @GetMapping("/nuevo")
    public String mostrarFormularioDeCrear(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "formulario-cliente";
    }

    // 4. Mostrar el formulario lleno para editar
    @GetMapping("/editar/{dpi}")
    public String mostrarFormularioDeEditar(@PathVariable String dpi, Model model) {
        clienteService.buscarPorDPI(dpi).ifPresent(cliente -> model.addAttribute("cliente", cliente));
        return "formulario-cliente";
    }

    // 5. Guardar (sirve tanto para crear como para editar en ClienteController.java)
    @PostMapping
    public String guardarOActualizar(@ModelAttribute Cliente cliente, Model model) {
        try {
            if(clienteService.existeDPI(cliente.getDpiCliente())){
                clienteService.actualizar(cliente.getDpiCliente(), cliente);
            } else {
                clienteService.guardar(cliente);
            }
            return "redirect:/clientes";
        } catch (Exception e) {
            // Si falla la validación, mostramos el error y no perdemos los datos
            model.addAttribute("error", e.getMessage());
            return "formulario-cliente";
        }
    }

    // 6. Eliminar (usando GET por simplicidad desde la vista)
    @GetMapping("/eliminar/{dpi}")
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