package ar.edu.utn.frbb.tup.repository;

import ar.edu.utn.frbb.tup.model.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por DNI
     *
     * @param dni DNI del cliente
     * @return Cliente encontrado o vacío
     */
    Optional<Cliente> findByDni(Long dni);

    /**
     * Verifica si existe un cliente con el DNI especificado
     *
     * @param dni DNI a verificar
     * @return true si existe, false si no
     */
    boolean existsByDni(Long dni);

    /**
     * Busca un cliente con cuentas y préstamos cargados
     *
     * @param dni DNI del cliente
     * @return Cliente completo
     */
    @Query("SELECT DISTINCT c FROM Cliente c " +
            "LEFT JOIN FETCH c.cuentas " +
            "LEFT JOIN FETCH c.prestamos " +
            "WHERE c.dni = :dni")
    Optional<Cliente> findByDniWithCuentasAndPrestamos(@Param("dni") Long dni);

    /**
     * Obtiene todos los clientes con sus cuentas
     *
     * @return Lista de clientes con cuentas
     */
    @Query("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.cuentas")
    List<Cliente> findAllWithCuentas();
}