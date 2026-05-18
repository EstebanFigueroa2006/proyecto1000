package modelo;

public class ProductoNoPerecedero extends Producto {
    private int porcentajeDesgaste;

    public ProductoNoPerecedero(String id, String nombre, int stockActual, int stockMinimo, double precioCompra, double precioVentaPublico, int porcentajeDesgaste) {
        super(id, nombre, stockActual, stockMinimo, precioCompra, precioVentaPublico);
        this.porcentajeDesgaste = porcentajeDesgaste;
    }

    public int getPorcentajeDesgaste() { return porcentajeDesgaste; }
    public void setPorcentajeDesgaste(int porcentajeDesgaste) { this.porcentajeDesgaste = porcentajeDesgaste; }

    @Override
    public boolean evaluarEstadoCritico() {
        return (getStockActual() <= getStockMinimo()) || (porcentajeDesgaste >= 80);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Tipo: Herramienta/Equipo | Desgaste: %d%%", porcentajeDesgaste);
    }
}