package ar.edu.utn.frbb.tup.persistence;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDao extends AbstractBaseDao{

    @Autowired
    CuentaDao cuentaDao;

    public Cliente find(long dni, boolean loadComplete) {
        if (getInMemoryDatabase().get(dni) == null)
            return null;
        Cliente cliente =   ((ClienteEntity) getInMemoryDatabase().get(dni)).toCliente();
        if (loadComplete) {
            for (Cuenta cuenta :
                    cuentaDao.getCuentasByCliente(dni)) {
                cliente.addCuenta(cuenta);
            }
        }
        return cliente;

    }

    /**
     * Obtiene todos los clientes del sistema
     *
     * @return Lista de todos los clientes con sus cuentas cargadas
     */
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();

        for (Object entity : getInMemoryDatabase().values()) {
            ClienteEntity clienteEntity = (ClienteEntity) entity;
            Cliente cliente = clienteEntity.toCliente();

            // Cargar las cuentas del cliente
            for (Cuenta cuenta : cuentaDao.getCuentasByCliente(cliente.getDni())) {
                cliente.addCuenta(cuenta);
            }

            clientes.add(cliente);
        }

        return clientes;
    }

    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
