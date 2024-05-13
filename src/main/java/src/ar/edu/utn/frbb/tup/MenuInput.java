package main.java.src.ar.edu.utn.frbb.tup;
import java.util.Scanner;

public class MenuInput {
    Scanner scanner = new Scanner(System.in);
    ClienteInput ClienteInput = new ClienteInput();
    CuentasBancariasInput CuentaBancaria = new CuentasBancariasInput();
    Historial historial = new Historial();
    boolean exit = false;

    public void showMenu() {
        while (!exit) {
            System.out.println("----------------------------------");
            System.out.println("Bienvenido a la app del Banco");
            System.out.println("1- Crear un nuevo Cliente");
            System.out.println("2- Crear una nueva Cuenta");
            System.out.println("3- Generar un Movimiento");
            System.out.println("4- Ver Historial de movimientos");
            System.out.println("5- Salir del programa");
            System.out.println("----------------------------------");
            System.out.print("Introduzca una opcion (1-5): ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    Cliente cliente = ClienteInput.registrarCliente();
                    if (cliente != null) {
                        System.out.println("El cliente se agrego exitosamente! :)");
                        System.out.println(cliente.toString());
                    }                    
                    break;
                case 2:
                    CuentasBancarias cuenta = CuentasBancariasInput.registrarCuenta();
                    if (cuenta != null) {
                        System.out.println("La cuenta se registro con exito!!");
                        System.out.println(cuenta.toString());
                    }
                    break;
                case 3:
                    MenuMovimientosInput menu = new MenuMovimientosInput();
                    menu.showMenu();
                    break;
                case 4:
                    Banco.getHistorial();
                    break;
                case 5:
                    System.out.println("Nos vemos pronto!:D");
                    return;
                default:
                    System.out.println("Opcion invalida, por favor ingrese un numero entre 1-4!");
                    break;
            }

        }
    }
}
