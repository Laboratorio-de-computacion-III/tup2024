package ar.edu.utn.frbb.tup.repository;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    /**
     * Busca cuentas por DNI del titular
     * @param dni DNI del titular
     * @return Lista de cuentas del cliente
     */
    @Query("SELECT c FROM Cuenta c WHERE c.titular.dni = :dni")
    List<Cuenta> findByTitularDni(@Param("dni") Long dni);

    /**
     * Busca una cuenta específica por DNI, tipo y moneda
     * @param dni DNI del titular
     * @param tipoCuenta Tipo de cuenta
     * @param moneda Moneda de la cuenta
     * @return Cuenta encontrada o vacío
     */
    @Query("SELECT c FROM Cuenta c WHERE c.titular.dni = :dni AND c.tipoCuenta = :tipoCuenta AND c.moneda = :moneda")
    Optional<Cuenta> findByTitularDniAndTipoCuentaAndMoneda(
            @Param("dni") Long dni,
            @Param("tipoCuenta") TipoCuenta tipoCuenta,
            @Param("moneda") TipoMoneda moneda);

    /**
     * Verifica si existe una cuenta con DNI, tipo y moneda específicos
     * @param dni DNI del titular
     * @param tipoCuenta Tipo de cuenta
     * @param moneda Moneda de la cuenta
     * @return true si existe, false si no
     */
    @Query("SELECT COUNT(c) > 0 FROM Cuenta c WHERE c.titular.dni = :dni AND c.tipoCuenta = :tipoCuenta AND c.moneda = :moneda")
    boolean existsByTitularDniAndTipoCuentaAndMoneda(
            @Param("dni") Long dni,
            @Param("tipoCuenta") TipoCuenta tipoCuenta,
            @Param("moneda") TipoMoneda moneda);

    /**
     * Busca cuentas por tipo de cuenta
     * @param tipoCuenta Tipo de cuenta
     * @return Lista de cuentas
     */
    List<Cuenta> findByTipoCuenta(TipoCuenta tipoCuenta);

    /**
     * Busca cuentas por moneda
     * @param moneda Moneda de la cuenta
     * @return Lista de cuentas
     */
    List<Cuenta> findByMoneda(TipoMoneda moneda);

    /**
     * Busca cuentas por DNI del titular y moneda
     * @param dni DNI del titular
     * @param moneda Moneda de la cuenta
     * @return Lista de cuentas en esa moneda
     */
    @Query("SELECT c FROM Cuenta c WHERE c.titular.dni = :dni AND c.moneda = :moneda")
    List<Cuenta> findByTitularDniAndMoneda(@Param("dni") Long dni, @Param("moneda") TipoMoneda moneda);

    /**
     * Obtiene el balance total de un cliente en una moneda específica
     * @param dni DNI del cliente
     * @param moneda Moneda
     * @return Suma total del balance
     */
    @Query("SELECT COALESCE(SUM(c.balance), 0) FROM Cuenta c WHERE c.titular.dni = :dni AND c.moneda = :moneda")
    Long getTotalBalanceByDniAndMoneda(@Param("dni") Long dni, @Param("moneda") TipoMoneda moneda);
}
