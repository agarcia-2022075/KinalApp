

package com.andregarcia.kinalapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // GET: Muestra la pantalla de Login
    @GetMapping("/login")
    public String mostrarLogin() {
        // Le dice a Spring que busque el archivo en templates/auth/login.html
        return "auth/login";
    }
}