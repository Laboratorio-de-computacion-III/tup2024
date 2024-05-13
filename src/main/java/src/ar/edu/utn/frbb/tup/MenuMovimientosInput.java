package main.java.src.ar.edu.utn.frbb.tup;
import java.util.Scanner;

public class MenuMovimientosInput extends Movimientos{
    Scanner scanner = new Scanner(System.in);
    boolean exit = true;

    public void showMenu() {
        System.out.print("Ingrese el ID de la cuenta: ");
        int id = scanner.nextInt();
        for (CuentasBancarias cuenta : cuentas) {
            if (cuenta.getId() == id) {
                exit = false;
                break;
            }
        }

        if (exit) {
            System.out.println("ID de la cuenta invalido. ");
        }

        while (!exit) {
            System.out.println("----------------------------------");
            System.out.println("Menu de Movimientos!");
            System.out.println("1- Depositar.");
            System.out.println("2- Extraer.");
            System.out.println("3- Transferir.");
            System.out.println("4- Ver saldo.");
            System.out.println("5- Volver al menu de inicio");
            System.out.println("----------------------------------");
            System.out.print("Introduzca una opcion (1-5): ");

            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    Movimientos.depositar(id);
                    break;
                case 2:
                    Movimientos.extraer(id);
                    break;
                case 3:
                    Movimientos.transferir(id);
                    break;
                case 4:
                    System.out.println(Movimientos.consultarSaldo(id));
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Opcion invalida! Ingrese un numero del 1-5.");
                    break;
            }
        }
    }
}

