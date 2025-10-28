package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import ar.edu.utn.frbb.tup.persistence.repository.ClienteJpaRepository;
import ar.edu.utn.frbb.tup.persistence.repository.CuentaJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CuentaDao {

    @Autowired
    private CuentaJpaRepository cuentaRepository;

    @Autowired
    private ClienteJpaRepository clienteRepository;

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        ClienteEntity titular = clienteRepository.getReferenceById(cuenta.getTitular().getDni());
        entity.setTitular(titular);
        cuentaRepository.save(entity);
    }

    public Cuenta find(long id) {
        Optional<CuentaEntity> entity = cuentaRepository.findById(id);
        return entity.map(CuentaEntity::toCuenta).orElse(null);
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (CuentaEntity cuenta : cuentaRepository.findByTitular_Id(dni)) {
            cuentasDelCliente.add(cuenta.toCuenta());
        }
        return cuentasDelCliente;
    }
}
