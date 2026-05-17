package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Producto {
    private String id;
    private String nombre;
    private int stockActual;
    private int stockMinimo;
    private double precioCompra;
    private double precioVentaPublico; // Nuevo atributo PVP
    private String fechaCaducidad;
    private int porcentajeDesgaste;

    public Producto(String id, String nombre, int stockActual, int stockMinimo, double precioCompra, double precioVentaPublico, String fechaCaducidad, int porcentajeDesgaste) {
        this.id = id;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.precioCompra = precioCompra;
        this.precioVentaPublico = precioVentaPublico;
        this.fechaCaducidad = fechaCaducidad;
        this.porcentajeDesgaste = porcentajeDesgaste;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }
    public double getPrecioVentaPublico() { return precioVentaPublico; }
    public void setPrecioVentaPublico(double precioVentaPublico) { this.precioVentaPublico = precioVentaPublico; }
    public String getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(String fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
    public int getPorcentajeDesgaste() { return porcentajeDesgaste; }
    public void setPorcentajeDesgaste(int porcentajeDesgaste) { this.porcentajeDesgaste = porcentajeDesgaste; }

    public boolean evaluarEstadoCritico() {
        if (stockActual <= stockMinimo) return true;
        if (porcentajeDesgaste >= 80) return true;

        if (!fechaCaducidad.equalsIgnoreCase("N/A")) {
            LocalDate fechaActual = LocalDate.of(2026, 5, 18);
            LocalDate caducidad = LocalDate.parse(fechaCaducidad);
            long diasParaCaducar = ChronoUnit.DAYS.between(fechaActual, caducidad);
            return diasParaCaducar <= 30;
        }
        return false;
    }

    @Override
    public String toString() {
        String infoExtra = fechaCaducidad.equalsIgnoreCase("N/A")
                ? String.format("Desgaste: %d%%", porcentajeDesgaste)
                : String.format("Vence: %s", fechaCaducidad);

        return String.format("ID: %s | Nombre: %s | Stock: %d | Mínimo: %d | Costo: $%.2f | PVP: $%.2f | %s",
                id, nombre, stockActual, stockMinimo, precioCompra, precioVentaPublico, infoExtra);
    }
}