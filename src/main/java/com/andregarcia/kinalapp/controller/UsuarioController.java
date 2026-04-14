package com.andregarcia.kinalapp.controller;

import com.andregarcia.kinalapp.entity.Usuario;
import com.andregarcia.kinalapp.service.IUsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // Cambiado de @RestController a @Controller para manejar vistas HTML
@RequestMapping("/usuarios")
public class UsuarioController {

    // Como buena práctica, la inyección de dependencias se hace por el constructor
    // El Controlador solo debe tener conexión con el servicio
    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET: Lista todos los usuarios y los envía a la vista 'usuarios/listar-usuarios.html'
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/listar-usuarios";
    }

    // GET: Filtra y muestra solo usuarios con estado activo
    @GetMapping("/activos")
    public String listarActivos(Model model) {
        model.addAttribute("usuarios", usuarioService.listarActivos());
        return "usuarios/listar-usuarios";
    }

    // GET: Busca un usuario específico para mostrar su perfil o detalles
    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Long id, Model model) {
        usuarioService.buscarPorId(id).ifPresent(usuario -> model.addAttribute("usuario", usuario));
        return "usuarios/detalle-usuario";
    }

    // POST: Recibe los datos del formulario (ModelAttribute) y guarda el nuevo usuario
    @PostMapping
    public String guardar(@ModelAttribute Usuario usuario) {
        try {
            usuarioService.guardar(usuario);
            // Tras un éxito, redirigimos a la lista para ver el cambio
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            // Si hay error de validación, enviamos un parámetro de error en la URL
            return "redirect:/usuarios?error=true";
        }
    }

    // PUT: Actualiza los datos de un usuario existente
    @PutMapping("/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Usuario usuario) {
        try {
            if (usuarioService.existeId(id)) {
                usuarioService.actualizar(id, usuario);
            }
            return "redirect:/usuarios";
        } catch (Exception e) {
            return "redirect:/usuarios?error=true";
        }
    }

    // DELETE: Elimina (o desactiva, según tu lógica de servicio) un usuario
    @DeleteMapping("/{id}")
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