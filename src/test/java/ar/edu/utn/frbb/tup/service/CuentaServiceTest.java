package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {

    @Mock
    CuentaDao cuentaDao;

    @Mock
    ClienteService clienteService;

    @InjectMocks
    CuentaService cuentaService;


    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void cuentaAlreadyExistsExceptionTest(){

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.DOLARES)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO)
                .setBalance(150000);
        cuenta.setNumeroCuenta(12345);

        when(cuentaDao.find(12345)).thenReturn(new Cuenta());

        assertThrows(CuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuenta, 38944251));

    }

    @Test
    public void tipoCuentaNoSoportadaTest(){

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.DOLARES)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                .setBalance(150000);
        cuenta.setNumeroCuenta(12345);

        assertThrows(TipoCuentaNotSupportedException.class, () -> cuentaService.darDeAltaCuenta(cuenta, 38944251));
    }

    @Test
    public void clienteConDosCuentasTest() throws TipoCuentaAlreadyExistsException {

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                .setBalance(150000);
        cuenta.setNumeroCuenta(12345);

        doThrow(TipoCuentaAlreadyExistsException.class).when(clienteService).agregarCuenta(cuenta,38944251);
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuenta,38944251));


    }

    @Test
    public void cuentaGuardadoExitosamente() throws ClienteAlreadyExistsException, TipoCuentaNotSupportedException, TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException {

        Cliente cliente = new Cliente();
        cliente.setDni(38944251);
        cliente.setNombre("Camila");
        cliente.setApellido("Piergentili");
        cliente.setFechaNacimiento(LocalDate.of(1997, 5, 11));
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);

        clienteService.darDeAltaCliente(cliente);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE)
                .setBalance(150000);
        cuenta.setNumeroCuenta(12345);

        cuentaService.darDeAltaCuenta(cuenta, 38944251);

        verify(cuentaDao, times(1)).save(cuenta);


    }



}
