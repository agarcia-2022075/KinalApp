package com.andregarcia.kinalapp.service;

import com.andregarcia.kinalapp.entity.Venta;
import java.util.List;
import java.util.Optional;

public interface IVentaService {
    List<Venta> listarTodos();
    List<Venta> listarActivos();
    Venta guardar(Venta venta);
    Optional<Venta> buscarPorId(Long id);
    Venta actualizar(Long id, Venta venta);
    void eliminar(Long id);
    boolean existeId(Long id);
}