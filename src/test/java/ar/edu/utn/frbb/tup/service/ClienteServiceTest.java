package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        Cliente clienteMenorDeEdad = new Cliente();
        clienteMenorDeEdad.setFechaNacimiento(LocalDate.of(2020, 2, 7));
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente();
        cliente.setFechaNacimiento(LocalDate.of(1978,3,25));
        cliente.setDni(29857643);
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        clienteService.darDeAltaCliente(cliente);

        verify(clienteDao, times(1)).save(cliente);
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(26456437, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }



    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta, pepeRino.getDni());

        verify(clienteDao, times(1)).save(pepeRino);

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(pepeRino, cuenta.getTitular());

    }


    @Test
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException {
        Cliente luciano = new Cliente();
        luciano.setDni(26456439);
        luciano.setNombre("Pepe");
        luciano.setApellido("Rino");
        luciano.setFechaNacimiento(LocalDate.of(1978, 3,25));
        luciano.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(luciano);

        clienteService.agregarCuenta(cuenta, luciano.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, luciano.getDni()));
        verify(clienteDao, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano, cuenta.getTitular());

    }


    //Agregar una CA$ y CC$ --> success 2 cuentas, titular peperino

    @Test
    public void testAgregarCuentasDistintosTipos() throws TipoCuentaAlreadyExistsException {

        Cliente cliente = new Cliente();
        cliente.setDni(38944251);
        cliente.setNombre("Camila");
        cliente.setApellido("Piergentili");
        cliente.setFechaNacimiento(LocalDate.of(1997, 5, 11));
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta().setMoneda(TipoMoneda.PESOS).setBalance(10000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(38944251, true)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta, cliente.getDni());

        Cuenta cuenta2 = new Cuenta().setMoneda(TipoMoneda.PESOS).setBalance(10000)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        clienteService.agregarCuenta(cuenta2, cliente.getDni());
        verify(clienteDao, times(2)).save(cliente);
        assertEquals(2, cliente.getCuentas().size());

    }


    //Agregar una CA$ y CAU$S --> success 2 cuentas, titular peperino...

    @Test
    public void testAgregarCuentasDolares() throws TipoCuentaAlreadyExistsException {

        Cliente cliente = new Cliente();
        cliente.setDni(38944251);
        cliente.setNombre("Camila");
        cliente.setApellido("Piergentili");
        cliente.setFechaNacimiento(LocalDate.of(1997, 5, 11));
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta().setMoneda(TipoMoneda.DOLARES).setBalance(10000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(38944251, true)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta, cliente.getDni());

        Cuenta cuenta2 = new Cuenta().setMoneda(TipoMoneda.DOLARES).setBalance(10000)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        clienteService.agregarCuenta(cuenta2, cliente.getDni());
        verify(clienteDao, times(2)).save(cliente);
        assertEquals(2, cliente.getCuentas().size());

    }


    //Testear clienteService.buscarPorDni

    @Test
    public void testBuscarPorDniPositivo(){
        Cliente cliente = new Cliente();
        cliente.setDni(38944251);
        cliente.setNombre("Camila");
        cliente.setApellido("Piergentili");
        cliente.setFechaNacimiento(LocalDate.of(1997, 5, 11));
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(38944251, true)).thenReturn(cliente);

        Cliente pepeRino = clienteService.buscarClientePorDni(38944251);
        assertEquals(cliente, pepeRino);

    }

    @Test
    public void testBuscarPorDniNegativo(){
        assertThrows(IllegalArgumentException.class, () -> clienteService.buscarClientePorDni(38944251));
    }
}