package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import ar.edu.utn.frbb.tup.persistence.repository.ClienteJpaRepository;
import ar.edu.utn.frbb.tup.persistence.repository.CuentaJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteDao {

    @Autowired
    private ClienteJpaRepository clienteRepository;

    @Autowired
    private CuentaJpaRepository cuentaRepository;

    public Cliente find(long dni, boolean loadComplete) {
        Optional<ClienteEntity> entityOpt = clienteRepository.findById(dni);
        if (entityOpt.isEmpty()) return null;
        Cliente cliente = entityOpt.get().toCliente();
        if (loadComplete) {
            List<CuentaEntity> cuentas = cuentaRepository.findByTitular_Id(dni);
            for (CuentaEntity ce : cuentas) {
                cliente.addCuenta(ce.toCuenta());
            }
        }
        return cliente;
    }

    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        clienteRepository.save(entity);
    }
}
