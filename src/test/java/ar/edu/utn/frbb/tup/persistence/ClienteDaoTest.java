package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteDaoTest {

    @Mock
    private ClienteJpaRepository clienteRepository;

    @Mock
    private CuentaJpaRepository cuentaRepository;

    @InjectMocks
    private ClienteDao clienteDao;

    private Cliente cliente;
    private ClienteEntity clienteEntity;

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
    }

    @Test
    public void testSaveClienteSuccess() {
        // Arrange
        when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(clienteEntity);

        // Act
        clienteDao.save(cliente);

        // Assert
        verify(clienteRepository, times(1)).save(any(ClienteEntity.class));
    }

    @Test
    public void testFindClienteByDniWithoutLoadingAccounts() {
        // Arrange
        long dni = 12345678;
        when(clienteRepository.findById(dni)).thenReturn(Optional.of(clienteEntity));

        // Act
        Cliente result = clienteDao.find(dni, false);

        // Assert
        assertNotNull(result);
        assertEquals(dni, result.getDni());
        assertEquals("Juan", result.getNombre());
        assertEquals("Perez", result.getApellido());
        assertEquals(TipoPersona.PERSONA_FISICA, result.getTipoPersona());
        assertTrue(result.getCuentas().isEmpty());
        verify(clienteRepository, times(1)).findById(dni);
        verify(cuentaRepository, never()).findByTitular_Id(anyLong());
    }

    @Test
    public void testFindClienteByDniWithLoadingAccounts() {
        // Arrange
        long dni = 12345678;
        when(clienteRepository.findById(dni)).thenReturn(Optional.of(clienteEntity));

        // Create mock cuenta entities
        CuentaEntity cuentaEntity1 = new CuentaEntity();
        cuentaEntity1.setNumeroCuenta(1001L);
        cuentaEntity1.setBalance(5000);
        cuentaEntity1.setTipoCuenta("CAJA_AHORRO");

        CuentaEntity cuentaEntity2 = new CuentaEntity();
        cuentaEntity2.setNumeroCuenta(1002L);
        cuentaEntity2.setBalance(10000);
        cuentaEntity2.setTipoCuenta("CUENTA_CORRIENTE");

        List<CuentaEntity> cuentas = new ArrayList<>();
        cuentas.add(cuentaEntity1);
        cuentas.add(cuentaEntity2);

        when(cuentaRepository.findByTitular_Id(dni)).thenReturn(cuentas);

        // Act
        Cliente result = clienteDao.find(dni, true);

        // Assert
        assertNotNull(result);
        assertEquals(dni, result.getDni());
        assertEquals("Juan", result.getNombre());
        assertEquals("Perez", result.getApellido());
        assertEquals(2, result.getCuentas().size());
        verify(clienteRepository, times(1)).findById(dni);
        verify(cuentaRepository, times(1)).findByTitular_Id(dni);
    }

    @Test
    public void testFindClienteByDniNotFound() {
        // Arrange
        long dni = 99999999;
        when(clienteRepository.findById(dni)).thenReturn(Optional.empty());

        // Act
        Cliente result = clienteDao.find(dni, false);

        // Assert
        assertNull(result);
        verify(clienteRepository, times(1)).findById(dni);
        verify(cuentaRepository, never()).findByTitular_Id(anyLong());
    }

    @Test
    public void testFindClienteWithLoadCompleteButNoAccounts() {
        // Arrange
        long dni = 12345678;
        when(clienteRepository.findById(dni)).thenReturn(Optional.of(clienteEntity));
        when(cuentaRepository.findByTitular_Id(dni)).thenReturn(new ArrayList<>());

        // Act
        Cliente result = clienteDao.find(dni, true);

        // Assert
        assertNotNull(result);
        assertEquals(dni, result.getDni());
        assertTrue(result.getCuentas().isEmpty());
        verify(clienteRepository, times(1)).findById(dni);
        verify(cuentaRepository, times(1)).findByTitular_Id(dni);
    }
}
