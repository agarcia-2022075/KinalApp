package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Usuario;
import com.andregarcia.kinalapp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuthController {

    private final IUsuarioService usuarioService;

    public AuthController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String email,
                                   @RequestParam String rol,
                                   Model model) {
        try {
            List<Usuario> todos = usuarioService.listarTodos();
            boolean existe = todos.stream().anyMatch(u -> u.getUsername().equals(username));
            if (existe) {
                model.addAttribute("error", "El nombre de usuario ya está en uso. Elige otro.");
                return "registro";
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setPassword(password); 
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setRol(rol);
            nuevoUsuario.setEstado(1); 

            usuarioService.guardar(nuevoUsuario);
            return "redirect:/login?registrado=true";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "registro";
        }
    }
}
