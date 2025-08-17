package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.TipoCuenta;

public class CuentaDto {
    private long numeroCliente;
    private TipoCuenta tipoCuenta;


    public long getNumeroCliente() {
        return numeroCliente;
    }

    public TipoCuenta getTipoCuenta(){
        return tipoCuenta;
    }
}
