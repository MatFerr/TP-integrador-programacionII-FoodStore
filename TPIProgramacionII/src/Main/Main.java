package Main;

import config_.DatabaseConfig;
import exception.ConexionFallidaException;
import java.util.Scanner;
import ui.CategoriaUI;
import ui.PedidoUI;
import ui.ProductoUI;
import ui.UsuarioIU;

public class Main {
    
    public static void main(String[] args) throws Exception{
        ejecutarDiagnosticoInicial();
        iniciarMenu();
    }
    public static void iniciarMenu() throws Exception{
    Scanner scanner = new Scanner(System.in);

        CategoriaUI categoriaUI = new CategoriaUI();
        ProductoUI productoUI = new ProductoUI();
        UsuarioIU usuarioUI = new UsuarioIU();
        PedidoUI pedidoUI = new PedidoUI();

        int opcion;
        do {
            System.out.println("\n==================================");
            System.out.println("   SISTEMA FOOD STORE - CONSOLA   ");
            System.out.println("==================================");
            System.out.println("1. Gestión de Categorías");
            System.out.println("2. Gestión de Productos");
            System.out.println("3. Gestión de Usuarios");
            System.out.println("4. Gestión de Pedidos");
            System.out.println("5. Ver configuracion de conexion");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> categoriaUI.mostrarMenuProducto();
                case 2 -> productoUI.mostrarMenuProducto();
                case 3 -> usuarioUI.mostrarMenuProducto();
                case 4 -> pedidoUI.mostrarMenuProducto();
                case 5 -> mostrarConfiguracionConexion();
                case 0 -> System.out.println("¡Gracias por utilizar el sistema!");
                default -> System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
        
    }
    private static void ejecutarDiagnosticoInicial() {
        System.out.println("==================================================");
        System.out.println("   INICIANDO SISTEMA CON DIAGNÓSTICO DE CONEXIÓN   ");
        System.out.println("==================================================");
        try {
            DatabaseConfig.probarConexion();
            System.out.println("Unidad de Persistencia: " + DatabaseConfig.getPersistenceUnitName());
        } catch (ConexionFallidaException e) {
            System.err.println(" ADVERTENCIA / ERROR DE CONEXIÓN:");
            System.err.println("  " + e.getMessage());
            System.err.println("  Pasos a revisar:");
            System.err.println("  1. Verifique que MySQL esté iniciado.");
            System.err.println("  2. Ejecute el script db/schema.sql para crear la BD.");
            System.err.println("  3. Revise credenciales en src/META-INF/persistence.xml.");
        }
        System.out.println("==================================================\n");
    }
    private static void mostrarConfiguracionConexion() {
        System.out.println("\n=========================================");
        System.out.println("      CONFIGURACIÓN DE CONEXIÓN          ");
        System.out.println("=========================================");
        System.out.println("Unidad de Persistencia activa: " + DatabaseConfig.getPersistenceUnitName());
        System.out.println("URL JDBC en uso : " + DatabaseConfig.getUrl());
        System.out.println("=========================================");
    }
    
}
