// Ubicación: C:\Spring_2022075\kinalapp\src\main\java\com\andregarcia\kinalapp\controller\AuthController.java

package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Usuario;
import com.andregarcia.kinalapp.service.IUsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {

    private final IUsuarioService usuarioService;

    public AuthController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET: Muestra la pantalla de Login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // POST: Procesa el formulario cuando el usuario hace clic en "Entrar"
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {

        // 1. PUERTA TRASERA / USUARIO MAESTRO (Para instalaciones nuevas)
        if ("admin".equals(username) && "admin123".equals(password)) {
            session.setAttribute("usuarioLogueado", "Admin Maestro");
            session.setAttribute("rolUsuario", "ADMINISTRADOR");
            return "redirect:/clientes";
        }

        // 2. Si no es el admin maestro, buscamos en la base de datos de MySQL
        List<Usuario> usuariosActivos = usuarioService.listarActivos();

        Optional<Usuario> usuarioValido = usuariosActivos.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (usuarioValido.isPresent()) {
            // Si lo encuentra en BD, guarda sus datos reales
            session.setAttribute("usuarioLogueado", usuarioValido.get().getUsername());
            session.setAttribute("rolUsuario", usuarioValido.get().getRol());
            return "redirect:/clientes";
        } else {
            // Si no es el maestro y tampoco está en la BD, lanza error
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    // GET: Para cerrar sesión
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Destruye la memoria de la sesión
        return "redirect:/login?logout=true";
    }
}