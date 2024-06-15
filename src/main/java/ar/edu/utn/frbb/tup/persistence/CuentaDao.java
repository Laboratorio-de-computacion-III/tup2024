package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.AccountNotSupportedExcepcion;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;

import java.util.*;

public class CuentaDao  extends AbstractBaseDao{
    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Cuenta find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(id)).toCuenta();
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Object object:
                getInMemoryDatabase().values()) {
            CuentaEntity cuenta = ((CuentaEntity) object);
            if (cuenta.getTitular().equals(dni)) {
                cuentasDelCliente.add(cuenta.toCuenta());
            }
        }
        return cuentasDelCliente;
    }


    public static final String[] CUENTAS_SOPORTADAS = new String[] {"CA$","CC$","CAU$S"};
    public static final Set<String> CONJUNTO_CUENTAS_SOPORTADAS = new HashSet<>(Arrays.asList(CUENTAS_SOPORTADAS));
    public boolean tipoDeCuentaSoportada(Cuenta cuenta) throws AccountNotSupportedExcepcion {
        if (CONJUNTO_CUENTAS_SOPORTADAS.contains(cuenta.getTipoCuenta().toString()))
            return true;
        throw  new AccountNotSupportedExcepcion("cuenta no soportada");
    }
}
