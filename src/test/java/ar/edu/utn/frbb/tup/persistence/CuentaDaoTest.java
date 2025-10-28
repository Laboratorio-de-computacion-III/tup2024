package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import ar.edu.utn.frbb.tup.persistence.repository.ClienteJpaRepository;
import ar.edu.utn.frbb.tup.persistence.repository.CuentaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaDaoTest {

    @Mock
    private CuentaJpaRepository cuentaRepository;

    @Mock
    private ClienteJpaRepository clienteRepository;

    @InjectMocks
    private CuentaDao cuentaDao;

    private Cliente cliente;
    private ClienteEntity clienteEntity;
    private Cuenta cuenta;

    @BeforeEach
    public void setUp() {
        // Create a test cliente
        cliente = new Cliente();
        cliente.setDni(12345678);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        cliente.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        cliente.setFechaAlta(LocalDate.now());

        // Create corresponding entity
        clienteEntity = new ClienteEntity(cliente);

        // Create a test cuenta
        cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1001L);
        cuenta.setBalance(5000);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setTitular(cliente);
    }

    @Test
    public void testSaveCuentaSuccess() {
        // Arrange
        when(clienteRepository.getReferenceById(cliente.getDni())).thenReturn(clienteEntity);
        when(cuentaRepository.save(any(CuentaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        cuentaDao.save(cuenta);

        // Assert
        verify(clienteRepository, times(1)).getReferenceById(cliente.getDni());
        verify(cuentaRepository, times(1)).save(any(CuentaEntity.class));
    }

    @Test
    public void testSaveCuentaLinksToExistingClient() {
        // Arrange
        when(clienteRepository.getReferenceById(cliente.getDni())).thenReturn(clienteEntity);
        when(cuentaRepository.save(any(CuentaEntity.class))).thenAnswer(invocation -> {
            CuentaEntity savedEntity = invocation.getArgument(0);
            // Verify that the titular was set
            assertNotNull(savedEntity.getTitular());
            assertEquals(clienteEntity, savedEntity.getTitular());
            return savedEntity;
        });

        // Act
        cuentaDao.save(cuenta);

        // Assert
        verify(clienteRepository, times(1)).getReferenceById(cliente.getDni());
        verify(cuentaRepository, times(1)).save(any(CuentaEntity.class));
    }

    @Test
    public void testGetCuentasByClienteReturnsAllAccounts() {
        // Arrange
        long dni = 12345678;

        // Create mock cuenta entities
        CuentaEntity cuentaEntity1 = new CuentaEntity();
        cuentaEntity1.setNumeroCuenta(1001L);
        cuentaEntity1.setBalance(5000);
        cuentaEntity1.setTipoCuenta("CAJA_AHORRO");
        cuentaEntity1.setFechaCreacion(LocalDateTime.now());

        CuentaEntity cuentaEntity2 = new CuentaEntity();
        cuentaEntity2.setNumeroCuenta(1002L);
        cuentaEntity2.setBalance(10000);
        cuentaEntity2.setTipoCuenta("CUENTA_CORRIENTE");
        cuentaEntity2.setFechaCreacion(LocalDateTime.now());

        CuentaEntity cuentaEntity3 = new CuentaEntity();
        cuentaEntity3.setNumeroCuenta(1003L);
        cuentaEntity3.setBalance(7500);
        cuentaEntity3.setTipoCuenta("CAJA_AHORRO");
        cuentaEntity3.setFechaCreacion(LocalDateTime.now());

        List<CuentaEntity> cuentaEntities = new ArrayList<>();
        cuentaEntities.add(cuentaEntity1);
        cuentaEntities.add(cuentaEntity2);
        cuentaEntities.add(cuentaEntity3);

        when(cuentaRepository.findByTitular_Id(dni)).thenReturn(cuentaEntities);

        // Act
        List<Cuenta> result = cuentaDao.getCuentasByCliente(dni);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1001L, result.get(0).getNumeroCuenta());
        assertEquals(5000, result.get(0).getBalance());
        assertEquals(TipoCuenta.CAJA_AHORRO, result.get(0).getTipoCuenta());
        assertEquals(1002L, result.get(1).getNumeroCuenta());
        assertEquals(10000, result.get(1).getBalance());
        assertEquals(TipoCuenta.CUENTA_CORRIENTE, result.get(1).getTipoCuenta());
        assertEquals(1003L, result.get(2).getNumeroCuenta());
        assertEquals(7500, result.get(2).getBalance());
        verify(cuentaRepository, times(1)).findByTitular_Id(dni);
    }

    @Test
    public void testGetCuentasByClienteReturnsEmptyListWhenNoAccounts() {
        // Arrange
        long dni = 12345678;
        when(cuentaRepository.findByTitular_Id(dni)).thenReturn(new ArrayList<>());

        // Act
        List<Cuenta> result = cuentaDao.getCuentasByCliente(dni);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cuentaRepository, times(1)).findByTitular_Id(dni);
    }

    @Test
    public void testGetCuentasByClienteWithSingleAccount() {
        // Arrange
        long dni = 12345678;

        CuentaEntity cuentaEntity = new CuentaEntity();
        cuentaEntity.setNumeroCuenta(1001L);
        cuentaEntity.setBalance(5000);
        cuentaEntity.setTipoCuenta("CAJA_AHORRO");
        cuentaEntity.setFechaCreacion(LocalDateTime.now());

        List<CuentaEntity> cuentaEntities = new ArrayList<>();
        cuentaEntities.add(cuentaEntity);

        when(cuentaRepository.findByTitular_Id(dni)).thenReturn(cuentaEntities);

        // Act
        List<Cuenta> result = cuentaDao.getCuentasByCliente(dni);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1001L, result.get(0).getNumeroCuenta());
        assertEquals(5000, result.get(0).getBalance());
        assertEquals(TipoCuenta.CAJA_AHORRO, result.get(0).getTipoCuenta());
        verify(cuentaRepository, times(1)).findByTitular_Id(dni);
    }
}
