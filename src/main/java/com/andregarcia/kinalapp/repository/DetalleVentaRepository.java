package com.andregarcia.kinalapp.repository;

import com.andregarcia.kinalapp.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    
}