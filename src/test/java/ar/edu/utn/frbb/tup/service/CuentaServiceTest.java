package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//    1 - cuenta existente
//    2 - cuenta no soportada
        // Chequear cuentas soportadas por el banco CA$ CC$ CAU$S
        // if (!tipoCuentaEstaSoportada(cuenta)) {...}
//    3 - cliente ya tiene cuenta de ese tipo
//    4 - cuenta creada exitosamente

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {

    //Se inicializa el mock de la clase CuentaDao y el mock de la clase ClienteService
    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    //Se inyecta la clase CuentaService
    @InjectMocks
    private CuentaService cuentaService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //TEST si la cuenta ya existe
    @Test
    public void testCuentaAlrreadyExists() {

        Cuenta cuenta = new Cuenta()
            .setMoneda(TipoMoneda.PESOS)
            .setBalance(500000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setNumeroCuenta(123456789);

        when(cuentaDao.find(123456789L)).thenReturn(new Cuenta());
        
        assertThrows(CuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuenta, 123456789L));
    
    }


    //TEST si la cuenta no es soportada por el banco
    @Test
    public void testTipoCuentaNoSoportada(){

        Cuenta cuenta = new Cuenta()
            .setMoneda(TipoMoneda.DOLARES)
            .setBalance(500000)
            .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setNumeroCuenta(123456789);

        assertThrows(TipoCuentaNotSupportedException.class, () -> cuentaService.darDeAltaCuenta(cuenta, 123456789)); 
    
    }

    //TEST si el cliente ya tiene una cuenta de ese tipo
    @Test
    public void testClienteTieneCuentaDeEsteTipo() throws ClienteAlreadyExistsException{
        
        Cliente cliente = new Cliente();
        cliente.setFechaNacimiento(LocalDate.of(1980, 2, 2));
        cliente.setDni(123456789);
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);

        clienteService.darDeAltaCliente(cliente);

        Cuenta cuenta = new Cuenta()
            .setMoneda(TipoMoneda.PESOS)
            .setBalance(500000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setNumeroCuenta(123456789);

    }

    //TEST si la cuenta se crea con exito
    @Test
    public void TestCuentaCreadaConExito() throws ClienteAlreadyExistsException, CuentaAlreadyExistsException, TipoCuentaNotSupportedException, TipoCuentaAlreadyExistsException{
        
        Cliente cliente = new Cliente();
        cliente.setFechaNacimiento(LocalDate.of(1980, 2, 2));
        cliente.setDni(123456789);
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);

        clienteService.darDeAltaCliente(cliente);

        Cuenta cuenta = new Cuenta()
            .setMoneda(TipoMoneda.PESOS)
            .setBalance(500000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setNumeroCuenta(123456789);

        cuentaService.darDeAltaCuenta(cuenta, 123456789);

        verify(cuentaDao, times(1)).save(cuenta);

    }

}