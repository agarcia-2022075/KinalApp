package com.andregarcia.kinalapp.service;

import com.andregarcia.kinalapp.entity.DetalleVenta;
import com.andregarcia.kinalapp.entity.Producto;
import com.andregarcia.kinalapp.repository.DetalleVentaRepository;
import com.andregarcia.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository, ProductoRepository productoRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        validarRelaciones(detalleVenta);
        calcularValores(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorId(Long id) {
        return detalleVentaRepository.findById(id);
    }

    @Override
    public DetalleVenta actualizar(Long id, DetalleVenta detalleVenta) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new RuntimeException("El detalle de venta no se encontró con el código: " + id);
        }
        detalleVenta.setCodigoDetalleVenta(id);
        validarRelaciones(detalleVenta);
        calcularValores(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }
    @Override
    public void eliminar(Long id) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new RuntimeException("El detalle de venta no se encontró con el código: " + id);
        }
        detalleVentaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeId(Long id) {
        return detalleVentaRepository.existsById(id);
    }
    private void validarRelaciones(DetalleVenta detalleVenta) {
        if (detalleVenta.getVenta() == null || detalleVenta.getVenta().getCodigoVenta() == null) {
            throw new IllegalArgumentException("El detalle debe estar asociado a una Venta (Código obligatorio).");
        }
        if (detalleVenta.getProducto() == null || detalleVenta.getProducto().getCodigoProducto() == null) {
            throw new IllegalArgumentException("El detalle debe estar asociado a un Producto (Código obligatorio).");
        }
        if (detalleVenta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad de productos debe ser mayor a cero.");
        }
    }


    private void calcularValores(DetalleVenta detalleVenta) {
        Producto productoDb = productoRepository.findById(detalleVenta.getProducto().getCodigoProducto())
                .orElseThrow(() -> new RuntimeException("El producto seleccionado no existe en el inventario."));
        detalleVenta.setPrecioUnitario(productoDb.getPrecio());
        BigDecimal subtotal = productoDb.getPrecio().multiply(new BigDecimal(detalleVenta.getCantidad()));
        detalleVenta.setSubtotal(subtotal);
    }
}