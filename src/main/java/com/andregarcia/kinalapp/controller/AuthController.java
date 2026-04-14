// Ubicación: C:\Spring_2022075\kinalapp\src\main\java\com\andregarcia\kinalapp\controller\AuthController.java

package com.andregarcia.kinalapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

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

        // 1.  un usuario de prueba estático por ahora
        if ("admin".equals(username) && "admin123".equals(password)) {

            // 2. Si es correcto, guardamos el nombre en la "memoria" del servidor (Sesión)
            session.setAttribute("usuarioLogueado", username);

            // 3. Lo redirigimos a la pantalla de clientes (nuestro dashboard temporal)
            return "redirect:/clientes";

        } else {
            // 4. Si falla, le enviamos un mensaje de error a la vista HTML
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