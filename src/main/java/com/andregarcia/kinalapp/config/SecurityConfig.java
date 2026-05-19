package com.andregarcia.kinalapp.config;

import com.andregarcia.kinalapp.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(UsuarioRepository usuarioRepository, CustomAuthenticationSuccessHandler successHandler) {
        this.usuarioRepository = usuarioRepository;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (No requieren login)
                .requestMatchers("/login", "/registro", "/images/**", "/css/**", "/js/**").permitAll()
                
                // Rutas exclusivas para ADMINISTRADOR
                .requestMatchers("/usuarios/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/clientes/eliminar/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/productos/nuevo", "/productos/editar/**", "/productos/eliminar/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/ventas/anular/**").hasRole("ADMINISTRADOR")
                
                // El resto de rutas requieren al menos estar autenticado (Cajeros y Admins)
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/403") // Página personalizada de acceso denegado
            )
            .csrf(csrf -> csrf.disable()); 
            
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Usuario Maestro
            if ("admin".equals(username)) {
                return User.withUsername("admin")
                        .password("admin123")
                        .roles("ADMINISTRADOR")
                        .build();
            }

            // Usuarios de la Base de Datos
            return usuarioRepository.findByUsername(username)
                    .filter(u -> u.getEstado() == 1)
                    .map(u -> User.withUsername(u.getUsername())
                            .password(u.getPassword())
                            .roles(u.getRol())
                            .build())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
