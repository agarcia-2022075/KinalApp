package com.andregarcia.kinalapp.service;

import com.andregarcia.kinalapp.entity.Venta;
import com.andregarcia.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarActivos() {
        return ventaRepository.findByEstado(1);
    }

    @Override
    public Venta guardar(Venta venta) {
        validarVenta(venta);

        if (venta.getEstado() == 0) {
            venta.setEstado(1); // Activa por defecto
        }

        // Asignar fecha actual si no viene en el JSON
        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(LocalDate.now());
        }

        // Iniciar el total en 0 si no viene especificado
        if (venta.getTotal() == null) {
            venta.setTotal(BigDecimal.ZERO);
        }

        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public Venta actualizar(Long id, Venta venta) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("La venta no se encontró con el código: " + id);
        }
        venta.setCodigoVenta(id);
        validarVenta(venta);

        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(LocalDate.now());
        }

        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("La venta no se encontró con el código: " + id);
        }
        ventaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeId(Long id) {
        return ventaRepository.existsById(id);
    }

    // Validaciones estrictas
    private void validarVenta(Venta venta) {
        // Validar que la venta tenga un cliente asignado y que el DPI no sea nulo
        if (venta.getCliente() == null || venta.getCliente().getDpiCliente() == null || venta.getCliente().getDpiCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener un cliente asociado (DPI obligatorio).");
        }

        // Validar que la venta tenga un usuario (vendedor) asignado
        if (venta.getUsuario() == null || venta.getUsuario().getCodigoUsuario() == null) {
            throw new IllegalArgumentException("La venta debe tener un usuario asociado (Código de empleado obligatorio).");
        }

        // Validar que el total no sea negativo
        if (venta.getTotal() != null && venta.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total de la venta no puede ser negativo.");
        }
    }
}