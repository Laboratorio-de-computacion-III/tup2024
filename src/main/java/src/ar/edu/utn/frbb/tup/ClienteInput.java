package main.java.src.ar.edu.utn.frbb.tup;
import java.time.LocalDate;
import java.util.Scanner;

public class ClienteInput extends Banco{
    Scanner scanner = new Scanner(System.in);

    public Cliente registrarCliente() {
        Cliente cliente = new Cliente();

        System.out.print("Ingrese el nombre del cliente: ");
        String nombre = scanner.nextLine();                                     //Lectura nombre
        cliente.setNombre(nombre);  

        System.out.print("Ingrese el apellido del cliente: ");
        String apellido = scanner.nextLine();                                       //Lectura apellido
        cliente.setApellido(apellido);

        System.out.print("Ingrese su DNI: ");
        String dni = scanner.nextLine();                                    //Lectura dni (identificador)
        cliente.setDni(dni);
        
        System.out.print("Ingrese la dirección del cliente: ");
        String direccion = scanner.nextLine();                                    //Lectura de direccion
        cliente.setDireccion(direccion);    

        System.out.print("Ingrese el teléfono del cliente: ");
        String telefono = scanner.nextLine();                           //Lectura de tel
        cliente.setTel(telefono);   

        System.out.print("Ingrese la fecha de alta del cliente (Formato: YYYY-MM-DD): ");
        LocalDate fechaNacimiento = null;
        boolean fechaValida = false;
        while (!fechaValida) {                                              //Lectura fecha nacimiento
            try {
                fechaNacimiento = LocalDate.parse(scanner.nextLine());
                fechaValida = true;
            } catch (Exception e) {
                System.out.print("Formato de fecha inválido. Ingrese la fecha en formato YYYY-MM-DD:");
            }
        }
        cliente.setFechaInicio(fechaNacimiento);


        clientes.add(cliente);  //Lo agreagmos a la lista
        return cliente;
    }

    public void eliminarCliente() {
        System.out.print("Ingrese el DNI del cliente a eliminar: ");
        String dni = scanner.nextLine();

        for (Cliente cliente : clientes) {  //Itera en la lista de clientes
            if (cliente.getDni().equals(dni)) {
                clientes.remove(cliente);
                System.out.println("Cliente eliminado correctamente.");
                return; //Sale del metodo
            }
        }
        System.out.println("No se encontró ningún cliente con el DNI especificado.");
    }

    public void modificarCliente() {
        boolean continuar = true;
    
        
        while (continuar) {
            System.out.println("Ingrese el DNI del cliente que desea modificar (o 'cancelar' para salir): ");
            String dni = scanner.nextLine();
    
            if (dni.equalsIgnoreCase("cancelar")) {
                System.out.println("Operación cancelada."); //Si quiere cancelar
                return;
            }

            Cliente clienteAModificar = null;
            for (Cliente cliente : clientes) {
                if (cliente.getDni().equals(dni)) { //Itera para ver si lo encuentra
                    clienteAModificar = cliente;
                    break;  
                }
            }
            
            if (clienteAModificar == null) {
                System.out.println("No se encontró ningún cliente con el DNI especificado.");
                continue; //vuelve al inicio del bucle para pedir otro DNI
            }
    
            System.out.println("Seleccione el campo que desea modificar:");
            System.out.println("1. Nombre");
            System.out.println("2. Apellido");
            System.out.println("3. Dirección");
            System.out.println("4. Teléfono");           //Lectura de opcion
            System.out.println("5. Cancelar");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nuevo nombre: ");
                    clienteAModificar.setNombre(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Ingrese el nuevo apellido: ");
                    clienteAModificar.setApellido(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Ingrese la nueva dirección: ");
                    clienteAModificar.setDireccion(scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Ingrese el nuevo teléfono: ");
                    clienteAModificar.setTel(scanner.nextLine());
                    scanner.nextLine();
                    break;
                case 5:
                    System.out.println("Operación cancelada.");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
            System.out.println("Cliente modificado correctamente.");
            
        }
    }
    
    
}