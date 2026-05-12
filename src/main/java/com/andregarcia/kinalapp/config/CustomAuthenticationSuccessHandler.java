package com.andregarcia.kinalapp.config;

import com.andregarcia.kinalapp.entity.Usuario;
import com.andregarcia.kinalapp.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;

    public CustomAuthenticationSuccessHandler(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        if ("admin".equals(username)) {
            session.setAttribute("usuarioLogueado", "Admin Maestro");
            session.setAttribute("rolUsuario", "ADMINISTRADOR");
        } else {
            Optional<Usuario> dbUser = usuarioRepository.findByUsername(username);
            if (dbUser.isPresent()) {
                session.setAttribute("usuarioLogueado", dbUser.get().getUsername());
                session.setAttribute("rolUsuario", dbUser.get().getRol());
            }
        }

        response.sendRedirect("/dashboard");
    }
}
