
package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Usuario;
import com.andregarcia.kinalapp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 1. Listar todos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "listar-usuarios";
    }

    // 2. Listar activos
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("usuarios", usuarioService.listarActivos());
        return "listar-usuarios";
    }

    // 3. Formulario Nuevo
    @GetMapping("/nuevo")
    public String mostrarFormularioDeCrear(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "formulario-usuario";
    }

    // 4. Formulario Editar
    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditar(@PathVariable Long id, Model model) {
        usuarioService.buscarPorId(id).ifPresent(usuario -> model.addAttribute("usuario", usuario));
        return "formulario-usuario";
    }

    // 5. Guardar o Actualizar
    @PostMapping
    public String guardarOActualizar(@ModelAttribute Usuario usuario) {
        try {
            // Spring Boot usará 'codigoUsuario' para decidir si hace INSERT o UPDATE
            usuarioService.guardar(usuario);
            return "redirect:/usuarios";
        } catch (Exception e) {
            return "redirect:/usuarios?error=true";
        }
    }

    // 6. Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            if (usuarioService.existeId(id)) {
                usuarioService.eliminar(id);
            }
            return "redirect:/usuarios";
        } catch (Exception e) {
            return "redirect:/usuarios?error=true";
        }
    }
}