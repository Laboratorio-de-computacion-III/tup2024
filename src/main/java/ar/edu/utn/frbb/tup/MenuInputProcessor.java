package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.utils.Cliente;

import java.util.Scanner;

public class MenuInputProcessor extends BaseInputProcessor{
    ClienteInputProcessor clienteInputProcessor = new ClienteInputProcessor();
    boolean exit = false;

    public void renderMenu(Banco banco) {

        while (!exit) {
            System.out.println("Bienveido a la aplicación de Banco!");
            System.out.println("1. Crear un nuevo Cliente");
            System.out.println("2. Crear una nueva Cuenta");
            System.out.println("3. Generar un movimiento");
            System.out.println("4. Salir");
            System.out.print("Ingrese su opción (1-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    Cliente c = clienteInputProcessor.ingresarCliente();
                    banco.getClientes().add(c);
                    break;
//            case 2:
//                createAccount();
//                break;
//            case 3:
//                performTransaction();
//                break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción inválida. Por favor seleccione 1-4.");
            }
            clearScreen();
        }
    }
}
