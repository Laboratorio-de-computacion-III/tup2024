package ar.edu.utn.frbb.tup.service;

import static ar.edu.utn.frbb.tup.model.TipoCuenta.CAJA_AHORRO;
import static ar.edu.utn.frbb.tup.model.TipoCuenta.CUENTA_CORRIENTE;
import static ar.edu.utn.frbb.tup.model.TipoMoneda.DOLARES;
import static ar.edu.utn.frbb.tup.model.TipoMoneda.PESOS;
import static ar.edu.utn.frbb.tup.model.TipoPersona.PERSONA_FISICA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.repository.ClienteRepository;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    private static final  BigDecimal BALANCE_INICIAL = BigDecimal.valueOf(10000);

    @InjectMocks
    private ClienteService clienteService;
    @Mock
    private ClienteRepository clienteRepository;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        ClienteDto clienteMenorDeEdad = new ClienteDto();
        clienteMenorDeEdad.setFechaNacimiento("2020-03-18");
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException {
        ClienteDto pepeRino = new ClienteDto();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento("1978-03-18");
        pepeRino.setTipoPersona(PERSONA_FISICA.getDescripcion());

        when(clienteRepository.existsByDni(26456437L)).thenReturn(false);
        when(clienteRepository.save(org.mockito.ArgumentMatchers.any(Cliente.class))).thenAnswer(
                i -> i.getArguments()[0]);

        Cliente clienteCreado = clienteService.darDeAltaCliente(pepeRino);

        verify(clienteRepository, times(1)).save(org.mockito.ArgumentMatchers.any(Cliente.class));

        assertEquals("Pepe", clienteCreado.getNombre());
        assertEquals("Rino", clienteCreado.getApellido());
        assertEquals(26456437, clienteCreado.getDni());
        assertEquals(LocalDate.of(1978, 3, 18), clienteCreado.getFechaNacimiento());
        assertEquals(PERSONA_FISICA, clienteCreado.getTipoPersona());

    }

    @Test
    public void testClienteAlreadyExistsException() {
        ClienteDto pepeRino = new ClienteDto();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento("1978-03-18");
        pepeRino.setTipoPersona(PERSONA_FISICA.toString());

        when(clienteRepository.existsByDni(26456437L)).thenReturn(true);

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }


    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException {
        Cliente pepeRino = createClienteForTesting();
        Cuenta cuenta = createCuentaForTesting(PESOS, BALANCE_INICIAL, CAJA_AHORRO);

        when(clienteRepository.findByDni(26456439L)).thenReturn(Optional.of(pepeRino));

        clienteService.agregarCuenta(cuenta, pepeRino.getDni());

        verify(clienteRepository, times(1)).save(pepeRino);

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(pepeRino, cuenta.getTitular());

    }

    @Test
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException {
        Cliente luciano = createClienteForTesting();
        Cuenta cuenta = createCuentaForTesting(PESOS, BALANCE_INICIAL, CAJA_AHORRO);

        when(clienteRepository.findByDni(26456439L)).thenReturn(Optional.of(luciano));

        clienteService.agregarCuenta(cuenta, luciano.getDni());

        Cuenta cuenta2 = createCuentaForTesting(PESOS, BALANCE_INICIAL, CAJA_AHORRO);

        assertThrows(TipoCuentaAlreadyExistsException.class,
                () -> clienteService.agregarCuenta(cuenta2, luciano.getDni()));
        verify(clienteRepository, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano, cuenta.getTitular());

    }


    //Agregar una CA$ y CC$ --> success 2 cuentas, titular peperino
    @Test
    public void agregarCajaAhorroYPesosCuentaCorrienteSuccess() throws TipoCuentaAlreadyExistsException {
        Cliente pepeRino = createClienteForTesting();
        Cuenta cajaAhorro = createCuentaForTesting(PESOS, BALANCE_INICIAL, CAJA_AHORRO);
        Cuenta cuentaCorriente = createCuentaForTesting(PESOS, BALANCE_INICIAL, CUENTA_CORRIENTE);

        when(clienteRepository.findByDni(26456439L)).thenReturn(Optional.of(pepeRino));

        clienteService.agregarCuenta(cajaAhorro, pepeRino.getDni());
        clienteService.agregarCuenta(cuentaCorriente, pepeRino.getDni());

        verify(clienteRepository, times(2)).save(pepeRino);

        assertEquals(2, pepeRino.getCuentas().size());
        assertTrue(pepeRino.getCuentas().contains(cajaAhorro));
        assertTrue(pepeRino.getCuentas().contains(cuentaCorriente));
        assertEquals(pepeRino, cajaAhorro.getTitular());
        assertEquals(pepeRino, cuentaCorriente.getTitular());
    }

    //Agregar una CA$ y CAU$S --> success 2 cuentas, titular peperino...
    @Test
    public void agregarCajaAhorroPesosYCajaAhorroDolaresSuccess() throws TipoCuentaAlreadyExistsException {
        Cliente pepeRino = createClienteForTesting();
        Cuenta cajaAhorroPesos = createCuentaForTesting(PESOS, BALANCE_INICIAL, CAJA_AHORRO);
        Cuenta cajaAhorroDolares = createCuentaForTesting(DOLARES, BALANCE_INICIAL, CAJA_AHORRO);

        when(clienteRepository.findByDni(26456439L)).thenReturn(Optional.of(pepeRino));

        clienteService.agregarCuenta(cajaAhorroPesos, pepeRino.getDni());
        clienteService.agregarCuenta(cajaAhorroDolares, pepeRino.getDni());

        verify(clienteRepository, times(2)).save(pepeRino);

        assertEquals(2, pepeRino.getCuentas().size());
        assertTrue(pepeRino.getCuentas().contains(cajaAhorroPesos));
        assertTrue(pepeRino.getCuentas().contains(cajaAhorroDolares));
        assertEquals(pepeRino, cajaAhorroPesos.getTitular());
        assertEquals(pepeRino, cajaAhorroDolares.getTitular());
    }

    //Testear clienteService.buscarPorDni
    @Test
    public void buscarClientePorDniSuccess() {
        Cliente pepeRino = createClienteForTesting();

        when(clienteRepository.findByDniWithCuentasAndPrestamos(26456439L)).thenReturn(Optional.of(pepeRino));

        Cliente result = clienteService.buscarClientePorDni(26456439L);

        verify(clienteRepository, times(1)).findByDniWithCuentasAndPrestamos(26456439L);
        assertEquals(pepeRino, result);
    }


    private Cliente createClienteForTesting() {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        pepeRino.setTipoPersona(PERSONA_FISICA);
        return pepeRino;
    }

    private Cuenta createCuentaForTesting(TipoMoneda moneda, BigDecimal balance, TipoCuenta tipoCuenta) {
        return new Cuenta()
                .setMoneda(moneda)
                .setBalance(balance)
                .setTipoCuenta(tipoCuenta);
    }
}