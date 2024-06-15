package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotFoundException;
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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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
        clienteMenorDeEdad.setFechaNacimiento(LocalDate.of(2024, 2, 7));
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
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException, ClienteNotFoundException {
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
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException, ClienteNotFoundException {
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

    @Test
    public void testAgregarDosCuentasMismoCliente() throws ClienteAlreadyExistsException {
        Cliente pepeRino = createClientePepeRino();
        Cuenta cuentaCA = new Cuenta()
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO)
                .setBalance(454545)
                .setMoneda(TipoMoneda.PESOS)
                .setFechaCreacion(LocalDateTime.now());
        cuentaCA.setNumeroCuenta(1);
        Cuenta cuentaCC = new Cuenta()
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                .setBalance(454)
                .setMoneda(TipoMoneda.PESOS);
        cuentaCC.setNumeroCuenta(2);

        pepeRino.addCuenta(cuentaCA);
        pepeRino.addCuenta(cuentaCC);
        assertEquals(2, pepeRino.getCuentas().size());
        assertEquals(pepeRino.getDni(), cuentaCA.getTitular().getDni());
        assertEquals(pepeRino.getDni(), cuentaCC.getTitular().getDni());
    }
    @Test
    public void testAgregarDosCuentasCA_CAUSD() throws ClienteAlreadyExistsException {
        Cliente pepeRino = createClientePepeRino();
        Cuenta cuentaCA = new Cuenta()
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO)
                .setBalance(454545)
                .setMoneda(TipoMoneda.PESOS)
                .setFechaCreacion(LocalDateTime.now());
        cuentaCA.setNumeroCuenta(1);
        Cuenta cuentaCAUSD = new Cuenta()
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO)
                .setBalance(454)
                .setMoneda(TipoMoneda.DOLARES);
        cuentaCAUSD.setNumeroCuenta(2);

        pepeRino.addCuenta(cuentaCA);
        pepeRino.addCuenta(cuentaCAUSD);
        assertEquals(2, pepeRino.getCuentas().size());
        assertEquals(pepeRino.getDni(), cuentaCA.getTitular().getDni());
        assertEquals(pepeRino.getDni(), cuentaCAUSD.getTitular().getDni());

    }
    @Test
    public void testBuscarPorDNI() throws ClienteAlreadyExistsException, ClienteNotFoundException {
        Cliente pepeRino = createClientePepeRino();
        when (clienteDao.find(pepeRino.getDni(), true)).thenReturn(pepeRino);
        assertEquals(pepeRino.getDni(), clienteService.buscarClientePorDni(pepeRino.getDni()).getDni());

        when (clienteDao.find(pepeRino.getDni(), true)).thenReturn(null);
        assertThrows(ClienteNotFoundException.class, () -> clienteService.buscarClientePorDni(pepeRino.getDni()));
    }

    private Cliente  createClientePepeRino() throws ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(11222333);
        pepeRino.setNombre("pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);
        pepeRino.setFechaNacimiento(LocalDate.of(1978,3,25));
        clienteService.darDeAltaCliente(pepeRino);
        return pepeRino;
    }

}









