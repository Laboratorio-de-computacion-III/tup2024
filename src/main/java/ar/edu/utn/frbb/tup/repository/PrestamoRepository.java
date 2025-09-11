package ar.edu.utn.frbb.tup.repository;

import ar.edu.utn.frbb.tup.model.Prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /**
     * Busca todos los préstamos de un cliente
     *
     * @param numeroCliente DNI del cliente
     * @return Lista de préstamos del cliente
     */
    List<Prestamo> findByNumeroCliente(Long numeroCliente);


}