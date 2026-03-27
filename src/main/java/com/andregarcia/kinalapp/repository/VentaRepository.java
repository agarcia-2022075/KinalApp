package com.andregarcia.kinalapp.repository;

import com.andregarcia.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    // buscar solo las ventas activas
    List<Venta> findByEstado(int estado);
}