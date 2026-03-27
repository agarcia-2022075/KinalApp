package com.andregarcia.kinalapp.repository;

import com.andregarcia.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    //metodo para buscar solo los productos activos
    List<Producto> findByEstado(int estado);
}