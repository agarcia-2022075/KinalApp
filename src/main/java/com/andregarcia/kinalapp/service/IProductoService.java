package com.andregarcia.kinalapp.service;

import com.andregarcia.kinalapp.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<Producto> listarTodos();
    List<Producto> listarActivos();
    Producto guardar(Producto producto);
    Optional<Producto> buscarPorId(Long id);
    Producto actualizar(Long id, Producto producto);
    void eliminar(Long id);
    boolean existeId(Long id);
}

