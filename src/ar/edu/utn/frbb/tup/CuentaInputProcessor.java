package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.utils.Cliente; 

import ar.edu.utn.frbb.tup.utils.Cuenta;
import ar.edu.utn.frbb.tup.utils.Cliente;
import ar.edu.utn.frbb.tup.Banco;
import ar.edu.utn.frbb.tup.utils.TipoPersona;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CuentaInputProcessor extends BaseInputProcessor {

    private static final Scanner scanner = new Scanner(System.in);

    public Cuenta crearCuenta(List<Cliente> clientes) {
        long dniCliente;
        do {
            System.out.print("Ingrese el DNI del cliente para asociar la cuenta (8 dígitos numéricos): ");
            while (!scanner.hasNextLong()) {
                System.out.println("Por favor, ingrese un número válido para el DNI.");
                scanner.next(); // Consumir el valor inválido
            }
            dniCliente = scanner.nextLong();
        } while (dniCliente < 0 || String.valueOf(dniCliente).length() != 8);

        Cliente clienteAsociado = null;
        for (Cliente cliente : clientes) {
            if (cliente.getDni() == dniCliente) {
                clienteAsociado = cliente;
                break;
            }
        }

        if (clienteAsociado != null) {
            System.out.print("Ingrese el tipo de cuenta (A para caja de ahorro, C para cuenta corriente): ");
            String tipoCuentaStr = scanner.next().toUpperCase();

            // Validar el tipo de cuenta ingresado
            Cuenta.TipoCuenta tipoCuenta;
            if (tipoCuentaStr.equals("A")) {
                tipoCuenta = Cuenta.TipoCuenta.CAJA_AHORRO;
            } else if (tipoCuentaStr.equals("C")) {
                tipoCuenta = Cuenta.TipoCuenta.CUENTA_CORRIENTE;
            } else {
                System.out.println("Tipo de cuenta inválido.");
                return null;
            }

            double saldoInicial;
            do {
                System.out.print("Ingrese el saldo inicial de la cuenta: ");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Por favor, ingrese un monto válido para el saldo.");
                    scanner.next(); // Consumir el valor inválido
                }
                saldoInicial = scanner.nextDouble();
            } while (saldoInicial < 0);

            scanner.nextLine(); // Consumir el salto de línea

            String numeroCuenta;
            do {
                System.out.print("Ingrese el número de cuenta (6 dígitos numéricos): ");
                numeroCuenta = scanner.nextLine();
            } while (numeroCuenta.length() != 6 || !numeroCuenta.matches("\\d+"));

            Cuenta cuenta = new Cuenta(numeroCuenta, saldoInicial, tipoCuenta, clienteAsociado);
            clienteAsociado.addCuenta(cuenta);

            System.out.println("Cuenta creada y asociada correctamente al cliente: " + clienteAsociado.getNombre() + " " + clienteAsociado.getApellido());
            return cuenta;
        } else {
            System.out.println("Error. No se ha encontrado ningún cliente con este DNI.");
            return null;
        }
    }

    public static void realizarDeposito(List<Cliente> clientes) {
        System.out.print("Ingrese el número de cuenta en el que desea realizar el depósito: ");
        String numeroCuenta = scanner.nextLine();

        Cuenta cuentaSeleccionada = null;
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getCuentas()) {
                if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                    cuentaSeleccionada = cuenta;
                    break;
                }
            }
            if (cuentaSeleccionada != null) {
                break;
            }
        }

        if (cuentaSeleccionada != null) {
            double monto;
            do {
                System.out.print("Ingrese el monto a depositar: ");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Por favor, ingrese un monto válido para el depósito.");
                    scanner.next(); // Consumir el valor inválido
                }
                monto = scanner.nextDouble();
            } while (monto < 0);

            scanner.nextLine(); // Consumir el salto de línea

            cuentaSeleccionada.depositar(monto);
            System.out.println("Depósito realizado exitosamente. Nuevo saldo: " + cuentaSeleccionada.getSaldo());
        } else {
            System.out.println("No se encontró ninguna cuenta con ese número.");
        }
    }

    public static void realizarRetiro(List<Cliente> clientes) {
        System.out.print("Ingrese el número de cuenta desde el que desea realizar el retiro: ");
        String numeroCuenta = scanner.nextLine();

        Cuenta cuentaSeleccionada = null;
        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getCuentas()) {
                if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                    cuentaSeleccionada = cuenta;
                    break;
                }
            }
            if (cuentaSeleccionada != null) {
                break;
            }
        }

        if (cuentaSeleccionada != null) {
            double monto;
            do {
                System.out.print("Ingrese el monto a retirar: ");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Por favor, ingrese un monto válido para el retiro.");
                    scanner.next(); // Consumir el valor inválido
                }
                monto = scanner.nextDouble();
            } while (monto < 0);

            scanner.nextLine(); // Consumir el salto de línea

            if (cuentaSeleccionada.retirar(monto)) {
                System.out.println("Retiro realizado exitosamente. Nuevo saldo: " + cuentaSeleccionada.getSaldo());
            } else {
                System.out.println("Saldo insuficiente para realizar el retiro.");
            }
        } else {
            System.out.println("No se encontró ninguna cuenta con ese número.");
        }
    }

// Método para consultar el saldo de una cuenta bancaria
public static void consultarSaldo(List<Cliente> clientes) {
    System.out.print("Ingrese el número de cuenta que desea consultar: ");
    String numeroCuenta = scanner.nextLine();

    // Buscar la cuenta correspondiente al número de cuenta ingresado
    Cuenta cuentaSeleccionada = null;
    for (Cliente cliente : clientes) {
        for (Cuenta cuenta : cliente.getCuentas()) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                cuentaSeleccionada = cuenta;
                break;
            }
        }
        if (cuentaSeleccionada != null) {
            break;
        }
    }

    // Si se encuentra la cuenta, mostrar su saldo
    if (cuentaSeleccionada != null) {
        System.out.println("Saldo de la cuenta " + numeroCuenta + ": " + cuentaSeleccionada.getSaldo());
    } else {
        System.out.println("No se encontró ninguna cuenta con ese número.");
    }
 }
}







