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

}