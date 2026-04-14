package com.andregarcia.kinalapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Obtenemos la sesión actual (sin crear una nueva si no existe)
        HttpSession session = request.getSession(false);

        // Verificamos si la sesión no existe o si no hay un usuario logueado
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            // Si no está logueado, lo redirigimos al login
            response.sendRedirect("/login");
            return false; // Bloqueamos el paso a la página solicitada
        }

        // Si tiene la variable "usuarioLogueado", lo dejamos pasar
        return true;
    }
}