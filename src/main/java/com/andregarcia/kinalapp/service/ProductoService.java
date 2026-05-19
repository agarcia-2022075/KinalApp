package com.andregarcia.kinalapp.service;

import com.andregarcia.kinalapp.entity.Producto;
import com.andregarcia.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByEstado(1);
    }

    @Override
    public Producto guardar(Producto producto) {
        validarProducto(producto);
        if (producto.getEstado() == 0) {
            producto.setEstado(1);
        }
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizar(Long id, Producto producto) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("El producto no se encontro con el codigo: " + id);
        }
        producto.setCodigoProducto(id);
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long id) {
        // Desactivación lógica en lugar de eliminación física
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El producto no se encontró con el código: " + id));
        producto.setEstado(0); // 0 = Inactivo
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeId(Long id) {
        return productoRepository.existsById(id);
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (producto.getNombreProducto().length() > 100) {
            throw new IllegalArgumentException("El nombre del producto no puede exceder los 100 caracteres.");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo o nulo.");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
    }
}