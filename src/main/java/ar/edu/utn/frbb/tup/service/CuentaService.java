package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CuentaService {

    CuentaDao cuentaDao = new CuentaDao();

    @Autowired
    ClienteService clienteService;

    //Se completa la funcion darDeAltaCuenta, con las condiciones necesarias.

    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException, TipoCuentaNotSupportedException, TipoCuentaAlreadyExistsException {
        
        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {

            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        
        }

        if (!tipoDeCuentaSoportada(cuenta)){

            throw new TipoCuentaNotSupportedException("La cuenta " + cuenta.getNumeroCuenta() + " no es soportada por el banco");
        
        }

        clienteService.agregarCuenta(cuenta, dniTitular);

        cuentaDao.save(cuenta);

    }

    public Cuenta find(long id) {

        return cuentaDao.find(id);

    }

    public boolean tipoDeCuentaSoportada(Cuenta cuenta) {

        TipoCuenta tipoCuenta = cuenta.getTipoCuenta();
        TipoMoneda moneda = cuenta.getMoneda();

        return (tipoCuenta == TipoCuenta.CUENTA_CORRIENTE && moneda == TipoMoneda.PESOS) || (tipoCuenta == TipoCuenta.CAJA_AHORRO && moneda == TipoMoneda.PESOS) || (tipoCuenta == TipoCuenta.CAJA_AHORRO && moneda == TipoMoneda.DOLARES);
    
    }
    
}