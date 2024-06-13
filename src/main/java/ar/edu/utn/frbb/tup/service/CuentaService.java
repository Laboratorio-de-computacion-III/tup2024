package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CuentaService {
    CuentaDao cuentaDao = new CuentaDao();

    @Autowired
    ClienteService clienteService;

    //Generar casos de test para darDeAltaCuenta
    //    1 - cuenta existente
    //    2 - cuenta no soportada
    //    3 - cliente ya tiene cuenta de ese tipo
    //    4 - cuenta creada exitosamente
    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException {
        if(cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        if(!tipoCuentaEstaSoportada(cuenta)){
            throw new TipoCuentaNotSupportedException("La cuenta " + cuenta.getNumeroCuenta() + " no es soportada");
        }

        clienteService.agregarCuenta(cuenta, dniTitular);
        cuentaDao.save(cuenta);
    }

    public boolean tipoCuentaEstaSoportada(Cuenta cuenta){
        String tipoCuenta = String.valueOf(cuenta.getTipoCuenta());
        String moneda = String.valueOf(cuenta.getMoneda());

        boolean soportada = false;

        if(tipoCuenta.equals("CAJA_AHORRO") && moneda.equals("PESOS")) {
            soportada = true;
        }
        if(tipoCuenta.equals("CUENTA_CORRIENTE") && moneda.equals("PESOS")) {
            soportada = true;
        }
        if(tipoCuenta.equals("CAJA_AHORRO") && moneda.equals("USD")){
            soportada = true;

        }
        return soportada;
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }
}
