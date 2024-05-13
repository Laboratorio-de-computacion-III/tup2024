package src.ar.edu.utn.frbb.tup;

import java.util.ArrayList;
import java.util.List;

public class Banco {
    public static List<Cliente> clientes = new ArrayList<>();
    public static List<Historial> historialBanco = new ArrayList<>();

    public static void getHistorial() {
        if (historialBanco.isEmpty()) {
            System.out.println("No hay movimientos que mostrar!");
        } else {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Lista de movimientos registrados: ");
            for (Historial historia : historialBanco) {
                System.out.println(historia.toString());
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
    }

    public static void getCuentas() {
        for (CuentasBancarias cuenta : Cliente.cuentas) {
            System.out.println(cuenta.toString());
        }
    }

    public static List<Cliente> obtenerListaClientes() {
        return clientes;
    }

}

