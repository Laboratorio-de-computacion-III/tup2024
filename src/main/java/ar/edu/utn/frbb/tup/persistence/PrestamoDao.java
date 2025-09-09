package ar.edu.utn.frbb.tup.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.persistence.entity.PrestamoEntity;

@Component
public class PrestamoDao extends AbstractBaseDao {

    @Override
    protected String getEntityName() {
        return "PRESTAMO";
    }

    /**
     * Guarda un préstamo en la base de datos en memoria
     *
     * @param prestamo Préstamo a guardar
     */
    public void save(Prestamo prestamo) {
        // Generar ID si no tiene
        if (prestamo.getId() == null) {
            prestamo.setId(Math.abs(new Random().nextLong()));
        }

        PrestamoEntity entity = new PrestamoEntity(prestamo);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    /**
     * Busca un préstamo por ID
     *
     * @param id ID del préstamo
     * @return Prestamo encontrado o null si no existe
     */
    public Prestamo find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((PrestamoEntity) getInMemoryDatabase().get(id)).toPrestamo();
    }

    /**
     * Busca todos los préstamos de un cliente
     *
     * @param numeroCliente DNI del cliente
     * @return Lista de préstamos del cliente
     */
    public List<Prestamo> findByCliente(long numeroCliente) {
        List<Prestamo> prestamosDelCliente = new ArrayList<>();
        for (Object object : getInMemoryDatabase().values()) {
            PrestamoEntity prestamoEntity = (PrestamoEntity) object;
            if (prestamoEntity.getNumeroCliente() == numeroCliente) {
                prestamosDelCliente.add(prestamoEntity.toPrestamo());
            }
        }
        return prestamosDelCliente;
    }
}