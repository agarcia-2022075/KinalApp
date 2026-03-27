package com.andregarcia.kinalapp.repository;

import com.andregarcia.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // buscar solo los usuarios activos
    List<Usuario> findByEstado(int estado);
}