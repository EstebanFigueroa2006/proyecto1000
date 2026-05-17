package interfaz;

import modelo.Producto;
import negocio.GestionInventario;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MenuConsola {
    private GestionInventario negocio;
    private Scanner scanner;
    private final LocalDate FECHA_ACTUAL = LocalDate.of(2026, 5, 18);

    public MenuConsola(GestionInventario negocio) {
        this.negocio = negocio;
        this.scanner = new Scanner(System.in);
    }

    public void iniciarSistema() {
        cargarDatosPredefinidos();
        int opcion = -1;
        do {
            imprimirMenu();
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println(" Error: Ingrese un valor numérico válido.");
            }
        } while (opcion != 0);
    }

    private void imprimirMenu() {
        System.out.println("\n=======================================================");
        System.out.println("---SISTEMA DE GESTIÓN DE INVENTARIO - OBRAS Y PINTURA---");
        System.out.println("=======================================================");
        System.out.println("1. Registrar Producto");
        System.out.println("2. Actualizar Producto");
        System.out.println("3. Vender Producto");
        System.out.println("4. Mostrar Inventario Completo");
        System.out.println("5. Consultar Alertas de Estado Crítico");
        System.out.println("6. Ver Valor Total Invertido en Bodega");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1: registrarProducto(); break;
            case 2: actualizarProducto(); break;
            case 3: venderProducto(); break;
            case 4: mostrarInventario(); break;
            case 5: mostrarAlertas(); break;
            case 6: mostrarValorTotal(); break;
            case 0: System.out.println("Saliendo del sistema de gestión..."); break;
            default: System.out.println(" Opción no válida. Intente de nuevo.");
        }
    }

    private void registrarProducto() {
        System.out.println("\n--- REGISTRO DE PRODUCTO ---");

        System.out.print("ID / Código: ");
        String id = scanner.nextLine();
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();

        int stock = solicitarEnteroPositivo("Stock Actual: ");
        int min = solicitarEnteroPositivo("Stock Mínimo Permitido: ");
        double precio = solicitarDoublePositivo("Precio de Compra (Costo) ($): ");
        double pvp = solicitarDoublePositivo("Precio de Venta al Público (PVP) ($): ");

        String fechaStr = solicitarFechaCaducidad();

        int desgaste = 0;
        if (fechaStr.equalsIgnoreCase("N/A")) {
            desgaste = solicitarPorcentaje("Porcentaje de Desgaste Actual (0-100): ");
        }

        Producto p = new Producto(id, nombre, stock, min, precio, pvp, fechaStr, desgaste);
        if (negocio.agregarProducto(p)) {
            System.out.println(" Producto registrado exitosamente.");
        }
    }

    private void actualizarProducto() {
        mostrarInventario();
        if (negocio.getListaProductos().isEmpty()) return;

        System.out.print("\nIngrese el ID del producto a actualizar: ");
        String id = scanner.nextLine();
        Producto p = negocio.buscarProducto(id);

        if (p == null) {
            System.out.println(" Error: Producto no encontrado.");
            return;
        }

        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- ACTUALIZAR: " + p.getNombre() + " ---");
            System.out.println("1. Actualizar Nombre");
            System.out.println("2. Actualizar Stock Actual");
            System.out.println("3. Actualizar Stock Mínimo");
            System.out.println("4. Actualizar Precio de Compra (Costo)");
            System.out.println("5. Actualizar Precio de Venta (PVP)");
            System.out.println("6. Actualizar Fecha de Caducidad");
            System.out.println("7. Actualizar Desgaste");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Nuevo nombre: ");
                        p.setNombre(scanner.nextLine());
                        break;
                    case 2:
                        p.setStockActual(solicitarEnteroPositivo("Nuevo Stock Actual: "));
                        break;
                    case 3:
                        p.setStockMinimo(solicitarEnteroPositivo("Nuevo Stock Mínimo: "));
                        break;
                    case 4:
                        p.setPrecioCompra(solicitarDoublePositivo("Nuevo Precio de Compra ($): "));
                        break;
                    case 5:
                        p.setPrecioVentaPublico(solicitarDoublePositivo("Nuevo PVP ($): "));
                        break;
                    case 6:
                        p.setFechaCaducidad(solicitarFechaCaducidad());
                        if (!p.getFechaCaducidad().equalsIgnoreCase("N/A")) {
                            p.setPorcentajeDesgaste(0); // Si caduca, reiniciamos el desgaste
                        }
                        break;
                    case 7:
                        p.setPorcentajeDesgaste(solicitarPorcentaje("Nuevo desgaste (0-100): "));
                        p.setFechaCaducidad("N/A"); // Si tiene desgaste, asumimos que no caduca
                        break;
                    case 0:
                        System.out.println("Actualización finalizada.");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            }
        }
    }

    private void venderProducto() {
        mostrarInventario();
        if (negocio.getListaProductos().isEmpty()) return;

        System.out.print("\nIngrese el ID del producto a vender: ");
        String id = scanner.nextLine();
        Producto p = negocio.buscarProducto(id);

        if (p == null) {
            System.out.println(" Error: Producto no encontrado.");
            return;
        }

        int cantidad = solicitarEnteroPositivo("Cantidad a vender: ");
        if (cantidad > p.getStockActual()) {
            System.out.println(" Error: No hay stock suficiente. Stock disponible: " + p.getStockActual());
        } else {
            p.setStockActual(p.getStockActual() - cantidad);
            System.out.println(" Venta exitosa. Nuevo stock de " + p.getNombre() + ": " + p.getStockActual());
            System.out.printf(" Total a cobrar al cliente: $%.2f\n", (cantidad * p.getPrecioVentaPublico()));
        }
    }

    // --- MÉTODOS DE VALIDACIÓN ---

    private int solicitarEnteroPositivo(String mensaje) {
        int valor = -1;
        while (valor < 0) {
            System.out.print(mensaje);
            try {
                valor = Integer.parseInt(scanner.nextLine());
                if (valor < 0) {
                    System.out.println(" Error: El valor no puede ser negativo. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Error: Ingrese un número entero válido.");
            }
        }
        return valor;
    }

    private double solicitarDoublePositivo(String mensaje) {
        double valor = -1;
        while (valor < 0) {
            System.out.print(mensaje);
            try {
                valor = Double.parseDouble(scanner.nextLine());
                if (valor < 0) {
                    System.out.println(" Error: El valor no puede ser negativo. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Error: Ingrese un valor numérico válido.");
            }
        }
        return valor;
    }

    private int solicitarPorcentaje(String mensaje) {
        int valor = -1;
        while (valor < 0 || valor > 100) {
            System.out.print(mensaje);
            try {
                valor = Integer.parseInt(scanner.nextLine());
                if (valor < 0 || valor > 100) {
                    System.out.println(" Error: El porcentaje debe estar entre 0 y 100.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Error: Ingrese un número válido.");
            }
        }
        return valor;
    }

    private String solicitarFechaCaducidad() {
        String fechaStr = "";
        boolean fechaValida = false;
        while (!fechaValida) {
            System.out.print("Fecha de Caducidad (AAAA-MM-DD) o ingrese 'N/A' si no caduca: ");
            fechaStr = scanner.nextLine().trim();

            if (fechaStr.equalsIgnoreCase("N/A")) {
                fechaValida = true;
            } else {
                try {
                    LocalDate fechaIngresada = LocalDate.parse(fechaStr);
                    if (fechaIngresada.isBefore(FECHA_ACTUAL)) {
                        System.out.println(" Error: La fecha ingresada ya pasó. La fecha actual es " + FECHA_ACTUAL + ". Producto expirado.");
                    } else {
                        fechaValida = true;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println(" Error: Formato incorrecto. Use AAAA-MM-DD o 'N/A'.");
                }
            }
        }
        return fechaStr;
    }

    // ----------------------------

    private void mostrarInventario() {
        System.out.println("\n---INVENTARIO EN BODEGA CENTRAL---");
        if (negocio.getListaProductos().isEmpty()) {
            System.out.println("La bodega se encuentra vacía.");
            return;
        }
        for (Producto p : negocio.getListaProductos()) {
            System.out.println(p);
        }
    }

    private void mostrarAlertas() {
        System.out.println("\n---ALERTAS DE ESTADO CRÍTICO---");
        var criticos = negocio.obtenerAlertasCriticas();
        if (criticos.isEmpty()) {
            System.out.println(" No hay productos en desabastecimiento o cercanos a vencer/dañarse.");
            return;
        }
        for (Producto p : criticos) {
            System.out.println(" CRÍTICO -> " + p);
        }
    }

    private void mostrarValorTotal() {
        System.out.printf("\n Valor total de la mercadería inmovilizada en inventario (Costo): $%.2f\n",
                negocio.calcularValorTotalInventario());
    }

    private void cargarDatosPredefinidos() {
        // Se agregó un PVP ficticio (85.00 costo -> 110.00 PVP | 120.00 costo -> 150.00 PVP)
        negocio.agregarProducto(new Producto("P01", "Pintura Látex", 50, 10, 85.00, 110.00, "2026-06-30", 0));
        negocio.agregarProducto(new Producto("H01", "Andamio Estructural", 12, 3, 120.00, 150.00, "N/A", 10));
    }
}