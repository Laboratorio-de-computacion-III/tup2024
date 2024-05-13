package main.java.src.ar.edu.utn.frbb.tup;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CuentasBancariasInput extends Cliente{
    static Scanner scanner = new Scanner(System.in);
    static int ultimoID = 0;

    //Metodo para obtener la lista de cuentas
    public static List<CuentasBancarias> obtenerListaCuentasBancarias() {
        return cuentas;
    }

    //hay un problema y no se pueden creas/mostrar dos cuentas distintas para el mismo cliente
    public static CuentasBancarias registrarCuenta() {
        CuentasBancarias cuenta = new CuentasBancarias();
        cuenta.setId(ultimoID + 1);                              //Input de ID unico           

        System.out.print("Ingrese el DNI del cliente: ");   //duenio de la cuenta 
        String dniCliente = scanner.nextLine();
        
        for (Cliente cliente : clientes) {
            if (dniCliente.equals(cliente.getDni())) {
                System.out.print("Ingrese el tipo de cuenta (Corriente(C) o Ahorro(A)): ");
                String respuesta = scanner.nextLine().toUpperCase();
                while (!respuesta.equals("C") && !respuesta.equals("A")) {
                    System.out.print("Por favor ingrese un tipo de cuenta válido. Corriente(C) o Ahorro(A): ");
                    respuesta = scanner.nextLine().toUpperCase();
                }                                                                           //Input del tipo de cuenta
                if (respuesta.equals("C")) {
                    cuenta.setTipocuenta(TipoCuenta.CORRIENTE);
                } else {
                    cuenta.setTipocuenta(TipoCuenta.AHORROS);
                }
        
                System.out.print("Ingrese el dinero disponible: $");
                double saldo = scanner.nextDouble();                                        //Input de saldo
                cuenta.setSaldo(saldo);
        
                cuenta.setFechaCreacion(LocalDate.now());                                    //momento dou
        
                cuentas.add(cuenta);
                return cuenta;
            }
        }
        System.out.println("El Cliente no existe, DNI invalido!");
        return null;     //en caso de que el cliente no exista    
    }

    public void eliminarCuenta() {
        System.out.print("Ingrese el ID de la cuenta a eliminar: ");
        int idCuentaAeliminar = scanner.nextInt();
        
        boolean cuentaEliminada = false;
        for (CuentasBancarias cuenta : cuentas) {
            if (cuenta.getId() == idCuentaAeliminar) {
                cuentas.remove(cuenta);
                cuentaEliminada = true;
                System.out.println("Cuenta bancaria eliminada correctamente.");
                break;
            }
        }
        if (!cuentaEliminada) {
            System.out.println("No se encontró ninguna cuenta bancaria asociada al cliente con el ID especificado.");
        }
    }

    public void listarCuentas() {
        if (cuentas.isEmpty()) {
            System.out.println("No hay cuentas bancarias registradas.");
        } else {
            System.out.println("Lista de cuentas bancarias registradas:");
            for (CuentasBancarias cuenta : cuentas) {
                System.out.println(cuenta);
            }
        }
    }

    
}

