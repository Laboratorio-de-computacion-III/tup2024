package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.AccountNotSupportedExcepcion;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CuentaService {

    private CuentaDao cuentaDao;
    @Autowired
    private ClienteService clienteService;
    public CuentaService(CuentaDao cuentaDao,ClienteService clienteService) {
        this.cuentaDao = cuentaDao;
        this.clienteService = clienteService;
    }

    public CuentaService() {
        cuentaDao = new CuentaDao();
    }

    //Generar casos de test para darDeAltaCuenta
    //    1 - cuenta existente
    //    2 - cuenta no soportada
    //    3 - cliente ya tiene cuenta de ese tipo
    //    4 - cuenta creada exitosamente



    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, AccountNotSupportedExcepcion, ClienteNotFoundException {
        tieneCuentaDeEsteTipo(cuenta,dniTitular);
        cuentaExistente(cuenta, dniTitular);
        clienteService.agregarCuenta(cuenta, dniTitular);
        cuentaDao.tipoDeCuentaSoportada(cuenta);

        cuentaDao.save(cuenta);

    }

    public void cuentaExistente(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistsException, ClienteNotFoundException {
        Cliente c = clienteService.buscarClientePorDni(dniTitular);
        if (c == null){
            throw new ClienteNotFoundException("El cliente no existe");
        }
        for (Cuenta cuentaCliente : c.getCuentas() ){
            if (cuentaCliente.getTipoCuenta() == cuenta.getTipoCuenta()){
                throw new TipoCuentaAlreadyExistsException("El cliente ya tiene una cuenta de este tipo");
            }
        }
    }

    public void tieneCuentaDeEsteTipo(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException {
        if(cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }


}
