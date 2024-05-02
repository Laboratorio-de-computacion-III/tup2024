package ar.edu.utn.frbb.tup;

import ar.edu.utn.frbb.tup.utils.Cliente;
import ar.edu.utn.frbb.tup.utils.Cuenta;
import ar.edu.utn.frbb.tup.utils.TipoPersona;

import java.time.LocalDate;
import java.time.LocalDateTime;

class Aplicacion {

    public static void main(String args[]) {

        Cuenta cuentaLuciano = new Cuenta().setNombre("1").setFechaCreacion(LocalDateTime.now()).setBalance(1835);
        Cuenta cuentaLuciano2 = new Cuenta().setNombre("2").setFechaCreacion(LocalDateTime.now()).setBalance(1500);

        Cliente cliente = new Cliente();
        cliente.setNombre("Luciano");
        cliente.setApellido("Salotto");
        cliente.setFechaNacimiento(LocalDate.of(1978, 2, 7));
        cliente.setBanco("Provincia");
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        cliente.setFechaAlta(LocalDate.now());
        cliente.addCuenta(cuentaLuciano);
        cliente.addCuenta(cuentaLuciano2);

        System.out.println("Luciano");

        System.out.println(cliente.getBalanceGeneral());

        Cuenta andoni = new Cuenta().setNombre("1").setFechaCreacion(LocalDateTime.now()).setBalance(1835);

        Cliente cliente2 = new Cliente();
        cliente2.setNombre("Andoni");
        cliente2.setApellido("Roque");
        cliente2.setFechaNacimiento(LocalDate.of(1992, 2, 21));
        cliente2.setBanco("Privincia");
        cliente2.setTipoPersona(TipoPersona.PERSONA_FISICA);
        cliente2.setFechaAlta(LocalDate.now());
        cliente2.addCuenta(andoni);
        
        System.out.println(cliente2.getBalanceGeneral());

        

    }
}
