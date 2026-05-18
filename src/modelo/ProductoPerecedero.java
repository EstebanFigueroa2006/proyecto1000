package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductoPerecedero extends Producto {
    private LocalDate fechaCaducidad;

    public ProductoPerecedero(String id, String nombre, int stockActual, int stockMinimo, double precioCompra, double precioVentaPublico, LocalDate fechaCaducidad) {
        super(id, nombre, stockActual, stockMinimo, precioCompra, precioVentaPublico);
        this.fechaCaducidad = fechaCaducidad;
    }

    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }

    @Override
    public boolean evaluarEstadoCritico() {
        if (getStockActual() <= getStockMinimo()) return true;
        LocalDate fechaActual = LocalDate.of(2026, 5, 18);
        long diasParaCaducar = ChronoUnit.DAYS.between(fechaActual, fechaCaducidad);
        return diasParaCaducar <= 30;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Tipo: Químico | Vence: %s", fechaCaducidad);
    }
}