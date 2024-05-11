package ar.edu.utn.frbb.tup;

import java.util.List;
import java.util.Scanner;

import ar.edu.utn.frbb.tup.utils.Cliente; 
import ar.edu.utn.frbb.tup.utils.Cuenta;
import java.io.IOException;

public class MenuInputProcessor extends BaseInputProcessor {
    
    private Scanner scanner = new Scanner(System.in);
    private ClienteInputProcessor clienteInputProcessor = new ClienteInputProcessor();
    private CuentaInputProcessor cuentaInputProcessor = new CuentaInputProcessor();
    
    private boolean exit = false;

    public void renderMenu(Banco banco) {
        while (!exit) {
            System.out.println("Bienvenido a la aplicación de Banco!");
            System.out.println("1.- Cliente");
            System.out.println("2.- Cuenta");
            System.out.println("3.- Salir del programa.");
            System.out.print("Ingrese su opción (1-3): ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            clearScreen();
            switch (choice) {
                case 1: // Opcion 1: Menú de Cliente
                    renderClienteMenu(banco);
                    break;
                case 2: // Opcion 2: Menú de Cuenta
                    renderCuentaMenu(banco.getClientes());
                    break;
                case 3: // Opcion 3: Salir del programa.
                    exit = true;
                    break;
                default:
                    System.out.println("Opción inválida. Por favor seleccione 1-3.");
                    break;
            }
        }
    }
    
    private void renderClienteMenu(Banco banco) {
        boolean exitClienteMenu = false;
        while (!exitClienteMenu) {
            System.out.println("Menú de Cliente");
            System.out.println("1.- Crear un nuevo Cliente.");
            System.out.println("2.- Mostrar los Clientes generados.");
            System.out.println("3.- Modificar un Cliente generado.");
            System.out.println("4.- Eliminar un Cliente generado.");
            System.out.println("5.- Volver al menú principal.");
            System.out.print("Ingrese su opción (1-5): ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            clearScreen();
            switch (choice) {
                case 1: // Opcion 1: Crear un nuevo Cliente.
                    Cliente c = clienteInputProcessor.ingresarCliente();
                    banco.getClientes().add(c);
                    break;
                case 2: // Opcion 2: Mostrar los clientes generados.
                    clienteInputProcessor.mostrarClientes(banco.getClientes());
                    break;
                case 3: // Opcion 3: Modificar un Cliente generado.
                    clienteInputProcessor.modificarCliente(banco.getClientes());
                    break;
                case 4: // Opcion 4: Eliminar un Cliente generado.
                    clienteInputProcessor.eliminarCliente(banco.getClientes());
                    break;
                case 5: // Opcion 5: Volver al menú principal.
                    exitClienteMenu = true;
                    break;
                default:
                    System.out.println("Opción inválida. Por favor seleccione 1-5.");
                    break;
            }
        }
    }
public void renderCuentaMenu(List<Cliente> clientes) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean exitCuentaMenu = false;

            while (!exitCuentaMenu) {
                System.out.println("Menú de Cuentas Bancarias");
                System.out.println("1.- Crear cuenta bancaria.");
                System.out.println("2.- Realizar depósito bancario.");
                System.out.println("3.- Realizar retiro bancario.");
                System.out.println("4.- Consultar saldo de la cuenta.");
                System.out.println("5.- Volver al menú principal.");
                System.out.print("Ingrese su opción (1-5): ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consumir newline character
                clearScreen();
                switch (choice) {
                    case 1:
                     Cuenta c = cuentaInputProcessor.crearCuenta(clientes);
                        break;
                    case 2:
                     CuentaInputProcessor.realizarDeposito(clientes);
                        break;
                    case 3:
                    CuentaInputProcessor.realizarRetiro(clientes);
                        break;
                    case 4:
                    CuentaInputProcessor.consultarSaldo(clientes);
                        break;
                    case 5:
                        exitCuentaMenu = true;
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor seleccione 1-5.");
                        break;
                }
            }
        }
    }


}

    


