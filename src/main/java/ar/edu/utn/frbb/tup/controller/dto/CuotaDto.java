package ar.edu.utn.frbb.tup.controller.dto;

import java.math.BigDecimal;

public class CuotaDto {

    private int cuotaNro;
    private BigDecimal monto;

    public CuotaDto() {
    }

    public CuotaDto(int cuotaNro, BigDecimal monto) {
        this.cuotaNro = cuotaNro;
        this.monto = monto;
    }

    public int getCuotaNro() {
        return cuotaNro;
    }

    public void setCuotaNro(int cuotaNro) {
        this.cuotaNro = cuotaNro;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}