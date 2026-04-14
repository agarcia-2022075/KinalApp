// Ubicación: C:\Spring_2022075\kinalapp\src\main\java\com\andregarcia\kinalapp\config\WebConfig.java

package com.andregarcia.kinalapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // Proteger TODAS las rutas del sistema
                .excludePathPatterns(
                        "/login",      // Permitir entrar a la vista de login
                        "/login.css**",     // Permitir cargar los estilos
                        "/js/**",      // Permitir cargar scripts
                        "/img/**"      // Permitir cargar imágenes
                );
    }
}