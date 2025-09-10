package ar.edu.utn.frbb.tup.repository;

import ar.edu.utn.frbb.tup.model.EstadoPrestamo;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /**
     * Busca todos los préstamos de un cliente
     * @param numeroCliente DNI del cliente
     * @return Lista de préstamos del cliente
     */
    List<Prestamo> findByNumeroCliente(Long numeroCliente);

    /**
     * Busca préstamos de un cliente por estado
     * @param numeroCliente DNI del cliente
     * @param estado Estado del préstamo
     * @return Lista de préstamos filtrados por estado
     */
    List<Prestamo> findByNumeroClienteAndEstado(Long numeroCliente, EstadoPrestamo estado);

    /**
     * Busca préstamos de un cliente en una moneda específica
     * @param numeroCliente DNI del cliente
     * @param moneda Moneda del préstamo
     * @return Lista de préstamos en esa moneda
     */
    List<Prestamo> findByNumeroClienteAndMoneda(Long numeroCliente, TipoMoneda moneda);

    /**
     * Busca préstamos activos (aprobados y no pagados) de un cliente
     * @param numeroCliente DNI del cliente
     * @return Lista de préstamos activos
     */
    @Query("SELECT p FROM Prestamo p WHERE p.numeroCliente = :numeroCliente AND p.estado IN ('APROBADO', 'PENDIENTE')")
    List<Prestamo> findPrestamosActivosByCliente(@Param("numeroCliente") Long numeroCliente);

    /**
     * Cuenta el número de préstamos activos de un cliente
     * @param numeroCliente DNI del cliente
     * @return Número de préstamos activos
     */
    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.numeroCliente = :numeroCliente AND p.estado IN ('APROBADO', 'PENDIENTE')")
    long countPrestamosActivosByCliente(@Param("numeroCliente") Long numeroCliente);

    /**
     * Busca préstamos por rango de fechas
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de préstamos en el rango
     */
    List<Prestamo> findByFechaSolicitudBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtiene el monto total prestado a un cliente
     * @param numeroCliente DNI del cliente
     * @return Suma total de préstamos aprobados
     */
    @Query("SELECT COALESCE(SUM(p.montoPrestamo), 0) FROM Prestamo p WHERE p.numeroCliente = :numeroCliente AND p.estado = 'APROBADO'")
    Double getTotalMontoPrestadoByCliente(@Param("numeroCliente") Long numeroCliente);

    /**
     * Obtiene el saldo total pendiente de un cliente
     * @param numeroCliente DNI del cliente
     * @return Suma total de saldos restantes
     */
    @Query("SELECT COALESCE(SUM(p.saldoRestante), 0) FROM Prestamo p WHERE p.numeroCliente = :numeroCliente AND p.estado = 'APROBADO'")
    Double getTotalSaldoPendienteByCliente(@Param("numeroCliente") Long numeroCliente);

    /**
     * Busca préstamos que vencen pronto (por implementar sistema de pagos)
     * @param numeroCliente DNI del cliente
     * @return Lista de préstamos próximos a vencer
     */
    @Query("SELECT p FROM Prestamo p WHERE p.numeroCliente = :numeroCliente AND p.estado = 'APROBADO' AND p.pagosRealizados < p.plazoMeses")
    List<Prestamo> findPrestamosConPagosPendientes(@Param("numeroCliente") Long numeroCliente);

    /**
     * Busca todos los préstamos por estado
     * @param estado Estado del préstamo
     * @return Lista de préstamos con ese estado
     */
    List<Prestamo> findByEstado(EstadoPrestamo estado);
}