package com.andregarcia.kinalapp.service;

import com.andregarcia.kinalapp.entity.DetalleVenta;
import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {
    List<DetalleVenta> listarTodos();
    DetalleVenta guardar(DetalleVenta detalleVenta);
    Optional<DetalleVenta> buscarPorId(Long id);
    DetalleVenta actualizar(Long id, DetalleVenta detalleVenta);
    void eliminar(Long id);
    boolean existeId(Long id);
}