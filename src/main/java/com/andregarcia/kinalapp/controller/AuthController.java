// Ubicación: C:\Spring_2022075\kinalapp\src\main\java\com\andregarcia\kinalapp\controller\AuthController.java

package com.andregarcia.kinalapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // GET: Muestra la pantalla de Login
    @GetMapping("/login")
    public String mostrarLogin() {
        // CORRECCIÓN: Le decimos a Spring que busque "login.html" directamente en la carpeta templates
        return "login";
    }
}