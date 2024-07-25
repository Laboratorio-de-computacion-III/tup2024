package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCuentaExistente() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        Cuenta cuentaExistente = new Cuenta();
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(123456789);
        cuentaDto.setMoneda("P");
        cuentaDto.setTipoCuenta("C");
        

        when(cuentaDao.find(anyLong())).thenReturn(cuentaExistente);
        assertThrows(CuentaAlreadyExistsException.class,
                () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    public void testTipoCuentaNoSoportada() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("D");

        doReturn(null).when(cuentaDao).find(anyLong());

        assertThrows(TipoCuentaNoSoportadaException.class, () -> {
            cuentaService.darDeAltaCuenta(cuentaDto);
        });
    }

    @Test
    public void testClienteYaTieneCuentaDeEseTipo() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        Cliente peperino = new Cliente();
        peperino.setDni(123456789);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.PESOS);
        peperino.addCuenta(cuenta);

        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(peperino.getDni());


        when(cuentaDao.find(anyLong())).thenReturn(null);
        doThrow(TipoCuentaAlreadyExistsException.class).when(clienteService).agregarCuenta(any(Cuenta.class), eq(peperino.getDni()));
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    public void testDarDeAltaCuentaTipoNuevo() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException, ClienteAlreadyExistsException {
        Cliente peperino = new Cliente();
        peperino.setDni(123456789);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");

        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("A");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(peperino.getDni());

        when(cuentaDao.find(anyLong())).thenReturn(null);
        cuentaService.darDeAltaCuenta(cuentaDto);

        verify(cuentaDao, times(1)).save(any(Cuenta.class));
    }
}