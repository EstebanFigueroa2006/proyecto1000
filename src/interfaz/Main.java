package interfaz;

import negocio.GestionInventario;

public class Main {
    public static void main(String[] args) {
        GestionInventario negocio = new GestionInventario();
        MenuConsola interfazUsuario = new MenuConsola(negocio);
        interfazUsuario.iniciarSistema();
    }
}