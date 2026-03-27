package com.andregarcia.kinalapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_venta")
    private Long codigoVenta;
    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "estado")
    private int estado;

    // Relacion con cliente (llave foránea)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Clientes_dpi_cliente", referencedColumnName = "dpi_cliente")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;
    // Relacion con usuario (Llave foránea)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuarios_codigo_usuario", referencedColumnName = "codigo_usuario")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    // constructor vacío
    public Venta() {
    }

    public Venta(Long codigoVenta, LocalDate fechaVenta, BigDecimal total, int estado, Cliente cliente, Usuario usuario) {
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    // Getter y Setters
    public Long getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(Long codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}