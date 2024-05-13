package src.ar.edu.utn.frbb.tup;
import java.util.Scanner;

public class Movimientos extends CuentasBancarias{
    static Scanner scanner = new Scanner(System.in);

    public static String consultarSaldo(int id) {
        return "Su saldo es: " + CuentasBancarias.buscarCuentaxID(id).getSaldo();
    }

    public static void depositar(int id) {
        double deposito;
        if (buscarCuentaxID(id) != null) {
            System.out.print("Introduzca la cantidad de dinero a depositar: $");
            deposito = scanner.nextDouble();
            if (deposito >= 0) {
                double saldoanterior = CuentasBancarias.buscarCuentaxID(id).getSaldo();
                CuentasBancarias.buscarCuentaxID(id).setSaldo(saldoanterior + deposito);
                Historial historialdeposito = new Historial(id, TipoDeOperacion.DEPOSITAR, deposito);
                historialBanco.add(historialdeposito);                                  //lo agregamos a la lista de movimientos
                System.out.println("Tu deposito fue exitoso, tu saldo actual es: $" + CuentasBancarias.buscarCuentaxID(id).getSaldo());
            }
            else {
                System.out.println("Cantidad de dinero NO valida!");
            }
            
        } else {
            System.out.println("No se ha encontrado la cuenta :(");
        }
    }

    public static void extraer(int id) {
        double extraccion;
        System.out.print("Ingrese la cantidad de dinero a extraer: $");
        extraccion = scanner.nextDouble();
        if (extraccion <= buscarCuentaxID(id).getSaldo() && extraccion > 1) {   //no se puede extraer menos de 1 peso
            double saldoanterior = CuentasBancarias.buscarCuentaxID(id).getSaldo();
            CuentasBancarias.buscarCuentaxID(id).setSaldo(saldoanterior - extraccion);
            Historial historialextraccion = new Historial(id, TipoDeOperacion.EXTRAER, extraccion);
            historialBanco.add(historialextraccion);                            //lo agregamos a la lista de movimientos
            System.out.println("Extraccion exitosa, por favor agarre el dinero. Su nuevo saldo es de: $" + CuentasBancarias.buscarCuentaxID(id).getSaldo());
        } else {
            System.out.println("No hay plata! :(");
        }
        
    }

    public static void transferir(int idTransfiere) {
        System.out.println("Ingrese ID al cual desea transferir: ");
        int idReceptor = scanner.nextInt();

        if (CuentasBancarias.buscarCuentaxID(idReceptor) != null && CuentasBancarias.buscarCuentaxID(idReceptor).getId() != idTransfiere) {  //Chequeo si existe la cuenta a transferir
            System.out.print("Ingrese cantidad a transferir: $");
            double cantidadATransferir = scanner.nextDouble();

            if (CuentasBancarias.buscarCuentaxID(idTransfiere).getSaldo() >= cantidadATransferir) {
                CuentasBancarias.buscarCuentaxID(idTransfiere).setSaldo(CuentasBancarias.buscarCuentaxID(idTransfiere).getSaldo() - cantidadATransferir); //resto
                CuentasBancarias.buscarCuentaxID(idReceptor).setSaldo(cantidadATransferir + CuentasBancarias.buscarCuentaxID(idReceptor).getSaldo());  //sumo
                Historial historialTrasferencia = new Historial(idTransfiere, TipoDeOperacion.TRANSFERIR, cantidadATransferir);
                historialBanco.add(historialTrasferencia);  //lo agregamos a la lista de movimientos
                System.out.println("Transferencia exitosa!! :D");
            } else {
                System.out.println("No hay plata :(");
            }
        } else {
            System.out.println("No se encontro el ID del receptor y no puede auto-transferirse:(");
        }
    }
}
