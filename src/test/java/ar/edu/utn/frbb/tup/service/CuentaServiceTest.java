package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;
    @Mock
    private ClienteDao clienteDao;
    @Mock
    private ClienteService clienteService;
    @InjectMocks
    private CuentaService cuentaService;

    @InjectMocks
    private ClienteService clienteServiceInject;

    @BeforeAll
    public void setup(){
        MockitoAnnotations.openMocks(this);
        this.cuentaService = new CuentaService(cuentaDao, clienteService);
        clienteServiceInject = new ClienteService(clienteDao);
    }

    @Test
    public void clienteCuentaExistente() throws ClienteAlreadyExistsException, TipoCuentaAlreadyExistsException, ClienteNotFoundException {
        Cliente pepeRino = createClientePepeRino();
        Cuenta cuenta = new Cuenta()
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                .setBalance(454545)
                .setMoneda(TipoMoneda.PESOS)
                .setFechaCreacion(LocalDateTime.now());
        cuenta.setNumeroCuenta(1);
        Cuenta cuenta1 = new Cuenta()
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                .setBalance(454)
                .setMoneda(TipoMoneda.PESOS);
        cuenta1.setNumeroCuenta(1);
        pepeRino.addCuenta(cuenta1);
        when(clienteDao.find(pepeRino.getDni(),true)).thenReturn(pepeRino);

        //verifico que no se llame a estemetodo
        assertThrows(TipoCuentaAlreadyExistsException.class, ()-> clienteServiceInject.agregarCuenta(cuenta, pepeRino.getDni()));
        //verifico que nunca se guaarde algo
        verify(cuentaDao, times(0)).save(cuenta);
    }
    @Test
    public void cuentaNoSoportada() throws ClienteAlreadyExistsException, AccountNotSupportedExcepcion, TipoCuentaAlreadyExistsException, ClienteNotFoundException, CuentaAlreadyExistsException {
        Cliente pepeRino = createClientePepeRino();
        Cuenta cuenta = new Cuenta()
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                .setBalance(454545)
                .setMoneda(TipoMoneda.PESOS)
                .setFechaCreacion(LocalDateTime.now());
        cuenta.setNumeroCuenta(1);
        when(cuentaDao.tipoDeCuentaSoportada(cuenta)).thenThrow( AccountNotSupportedExcepcion.class);
        when(clienteService.buscarClientePorDni(pepeRino.getDni())).thenReturn(pepeRino);
        assertThrows(AccountNotSupportedExcepcion.class, ()-> cuentaService.darDeAltaCuenta(cuenta,pepeRino.getDni()));
    }

    @Test
    public void cuentaExistente() throws ClienteAlreadyExistsException {
        Cliente pepeRino = createClientePepeRino();
        Cuenta cuenta = new Cuenta()
                        .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                        .setBalance(454545)
                        .setMoneda(TipoMoneda.PESOS)
                        .setFechaCreacion(LocalDateTime.now());
        cuenta.setNumeroCuenta(1);
        Cuenta cuenta1 = new Cuenta()
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                .setBalance(454)
                .setMoneda(TipoMoneda.PESOS);
        cuenta1.setNumeroCuenta(1);

        cuenta.setTitular(pepeRino);
        pepeRino.addCuenta(cuenta);

        when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(cuenta);
        assertThrows(CuentaAlreadyExistsException.class, () ->cuentaService.darDeAltaCuenta(cuenta1, pepeRino.getDni()));
    }



    @Test
    public void cuentaCreadaExito() throws ClienteAlreadyExistsException, AccountNotSupportedExcepcion, TipoCuentaAlreadyExistsException, ClienteNotFoundException, CuentaAlreadyExistsException {
        Cliente peperino = createClientePepeRino();
        Cuenta cuenta1 = new Cuenta()
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                .setBalance(454)
                .setMoneda(TipoMoneda.PESOS);
        cuenta1.setNumeroCuenta(1);
        when(clienteService.buscarClientePorDni(peperino.getDni())).thenReturn(peperino);
        cuentaService.darDeAltaCuenta(cuenta1, peperino.getDni());
        verify(cuentaDao, times(1)).save(cuenta1);
    }
    private Cliente  createClientePepeRino() throws ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(11222333);
        pepeRino.setNombre("pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);
        pepeRino.setFechaNacimiento(LocalDate.of(1978,3,25));
        return pepeRino;
    }


}
