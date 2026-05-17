package negocio;

import modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class GestionInventario {
    private List<Producto> listaProductos;
    private static final int MAX_CAPACIDAD_BODEGA = 1000;

    public GestionInventario() {
        this.listaProductos = new ArrayList<>();
    }

    public boolean agregarProducto(Producto nuevoProducto) {
        if (buscarProducto(nuevoProducto.getId()) != null) {
            System.out.println(" Error: Ya existe un producto registrado con el ID: " + nuevoProducto.getId());
            return false;
        }

        int stockTotalFuturo = calcularStockTotal() + nuevoProducto.getStockActual();
        if (stockTotalFuturo > MAX_CAPACIDAD_BODEGA) {
            System.out.println(" Error: Almacenamiento insuficiente. Límite de bodega (" + MAX_CAPACIDAD_BODEGA + ").");
            return false;
        }

        listaProductos.add(nuevoProducto);
        return true;
    }

    public Producto buscarProducto(String id) {
        for (Producto p : listaProductos) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    public List<Producto> getListaProductos() {
        return listaProductos;
    }

    private int calcularStockTotal() {
        int total = 0;
        for (Producto p : listaProductos) {
            total += p.getStockActual();
        }
        return total;
    }

    public double calcularValorTotalInventario() {
        double valorTotal = 0;
        for (Producto p : listaProductos) {
            valorTotal += (p.getStockActual() * p.getPrecioCompra());
        }
        return valorTotal;
    }

    public List<Producto> obtenerAlertasCriticas() {
        List<Producto> criticos = new ArrayList<>();
        for (Producto p : listaProductos) {
            if (p.evaluarEstadoCritico()) {
                criticos.add(p);
            }
        }
        return criticos;
    }
}