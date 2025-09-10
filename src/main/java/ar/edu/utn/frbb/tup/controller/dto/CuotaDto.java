package ar.edu.utn.frbb.tup.controller.dto;

public class CuotaDto {

    private int cuotaNro;
    private double monto;

    public CuotaDto() {
    }

    public CuotaDto(int cuotaNro, double monto) {
        this.cuotaNro = cuotaNro;
        this.monto = monto;
    }

    public int getCuotaNro() {
        return cuotaNro;
    }

    public void setCuotaNro(int cuotaNro) {
        this.cuotaNro = cuotaNro;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}