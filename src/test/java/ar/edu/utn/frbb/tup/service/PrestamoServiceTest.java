package ar.edu.utn.frbb.tup.service;

import static ar.edu.utn.frbb.tup.model.TipoCuenta.CAJA_AHORRO;
import static ar.edu.utn.frbb.tup.model.TipoMoneda.PESOS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CreditoRechazadoException;
import ar.edu.utn.frbb.tup.model.exception.PrestamoException;
import ar.edu.utn.frbb.tup.repository.PrestamoRepository;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrestamoServiceTest {

    private static final BigDecimal BALANCE_INICIAL = BigDecimal.valueOf(10000);
    private static final long NUMERO_CLIENTE = 12345678;
    private static final int PLAZO_MESES = 6;

    @InjectMocks
    private PrestamoService prestamoService;

    @Mock
    private ClienteService clienteService;
    @Mock
    private CuentaService cuentaService;
    @Mock
    private CalificacionCrediticiaService calificacionCrediticiaService;
    @Mock
    private PrestamoRepository prestamoRepository;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void solicitarPrestamoFailsWhenClienteNoTieneCuentaEnMonedaSolicitada() {
        PrestamoDto prestamoDto = createPrestamoDtoForTesting(NUMERO_CLIENTE, "DOLARES");

        Cliente cliente = new Cliente();
        cliente.setDni(NUMERO_CLIENTE);

        when(clienteService.buscarClientePorDni(NUMERO_CLIENTE)).thenReturn(cliente);
        when(cuentaService.obtenerCuentasPorClienteYMoneda(NUMERO_CLIENTE, TipoMoneda.DOLARES)).thenReturn(List.of());

        assertThrows(PrestamoException.class, () -> prestamoService.solicitarPrestamo(prestamoDto));
    }

    @Test
    public void solicitarPrestamoFailsWhenMonedaSolicitadaEsInvalida() {
        PrestamoDto prestamoDto = createPrestamoDtoForTesting(NUMERO_CLIENTE, "BITCOIN");

        assertThrows(PrestamoException.class, () -> prestamoService.solicitarPrestamo(prestamoDto));
    }

    @Test
    public void solicitarPrestamoFailsWhenClienteNotFound() {
        PrestamoDto prestamoDto = createPrestamoDtoForTesting(99999999, "PESOS");

        when(clienteService.buscarClientePorDni(99999999)).thenThrow(
                new IllegalArgumentException("Cliente no encontrado"));

        assertThrows(PrestamoException.class, () -> prestamoService.solicitarPrestamo(prestamoDto));
    }

    @Test
    public void solicitarPrestamoFailsWhenCalificacionCrediticiaRechazado() {
        PrestamoDto prestamoDto = createPrestamoDtoForTesting(NUMERO_CLIENTE, "PESOS");

        Cliente cliente = new Cliente();
        cliente.setDni(NUMERO_CLIENTE);

        Cuenta cuenta = createCuentaForTesting();

        cliente.addCuenta(cuenta);

        when(clienteService.buscarClientePorDni(NUMERO_CLIENTE)).thenReturn(cliente);
        when(cuentaService.obtenerCuentasPorClienteYMoneda(NUMERO_CLIENTE, PESOS)).thenReturn(List.of(cuenta));
        when(calificacionCrediticiaService.verificarCalificacionCrediticia(NUMERO_CLIENTE)).thenReturn(false);

        assertThrows(CreditoRechazadoException.class, () -> prestamoService.solicitarPrestamo(prestamoDto));
    }

    @Test
    public void solicitarPrestamoSucceedsWhenCumpleCondiciones() throws PrestamoException, CreditoRechazadoException {
        PrestamoDto prestamoDto = createPrestamoDtoForTesting(NUMERO_CLIENTE, "PESOS");

        Cliente cliente = new Cliente();
        cliente.setDni(NUMERO_CLIENTE);

        Cuenta cuenta = createCuentaForTesting();

        cliente.addCuenta(cuenta);

        when(clienteService.buscarClientePorDni(NUMERO_CLIENTE)).thenReturn(cliente);
        when(cuentaService.obtenerCuentasPorClienteYMoneda(NUMERO_CLIENTE, PESOS)).thenReturn(List.of(cuenta));
        when(calificacionCrediticiaService.verificarCalificacionCrediticia(NUMERO_CLIENTE)).thenReturn(true);

        PrestamoResponseDto response = prestamoService.solicitarPrestamo(prestamoDto);

        assertEquals("APROBADO", response.getEstado());
        assertEquals("El monto del préstamo fue acreditado en su cuenta", response.getMensaje());
        verify(prestamoRepository, times(1)).save(any(Prestamo.class));
        verify(cuentaService, times(1)).actualizarCuenta(cuenta);
    }

    private Cuenta createCuentaForTesting() {
        return new Cuenta()
                .setMoneda(PESOS)
                .setBalance(BALANCE_INICIAL)
                .setTipoCuenta(CAJA_AHORRO);
    }

    private PrestamoDto createPrestamoDtoForTesting(long numeroCliente,
            String moneda) {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(numeroCliente);
        prestamoDto.setMontoPrestamo(BALANCE_INICIAL);
        prestamoDto.setPlazoMeses(PLAZO_MESES);
        prestamoDto.setMoneda(moneda);
        return prestamoDto;
    }
}