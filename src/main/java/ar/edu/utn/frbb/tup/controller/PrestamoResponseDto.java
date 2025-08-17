package ar.edu.utn.frbb.tup.controller;

import java.util.List;

public class PrestamoResponseDto {

    private String estado;
    private String mensaje;
    private List<CuotaDto> planPagos;

    // Constructores
    public PrestamoResponseDto() {
    }

    public PrestamoResponseDto(String estado, String mensaje, List<CuotaDto> planPagos) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.planPagos = planPagos;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setPlanPagos(List<CuotaDto> planPagos) {
        this.planPagos = planPagos;
    }

    public List<CuotaDto> getPlanPagos() {
        return planPagos;
    }
}
