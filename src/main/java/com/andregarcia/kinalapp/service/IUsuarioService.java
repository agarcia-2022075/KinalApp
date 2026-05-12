package com.andregarcia.kinalapp.service;

import com.andregarcia.kinalapp.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listarTodos();
    List<Usuario> listarActivos();
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long id);
    boolean existeId(Long id);
}