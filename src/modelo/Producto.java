package modelo;

public abstract class Producto {
    private String id;
    private String nombre;
    private int stockActual;
    private int stockMinimo;
    private double precioCompra;
    private double precioVentaPublico;

    public Producto(String id, String nombre, int stockActual, int stockMinimo, double precioCompra, double precioVentaPublico) {
        this.id = id;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.precioCompra = precioCompra;
        this.precioVentaPublico = precioVentaPublico;
    }

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

    public abstract boolean evaluarEstadoCritico();

    @Override
    public String toString() {
        return String.format("ID: %s | Nombre: %s | Stock: %d | Mínimo: %d | Costo: $%.2f | PVP: $%.2f",
                id, nombre, stockActual, stockMinimo, precioCompra, precioVentaPublico);
    }
}
