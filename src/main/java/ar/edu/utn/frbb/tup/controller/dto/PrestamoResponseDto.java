package ar.edu.utn.frbb.tup.controller.dto;

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

    // Getters que faltaban
    public String getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<CuotaDto> getPlanPagos() {
        return planPagos;
    }

    // Setters
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setPlanPagos(List<CuotaDto> planPagos) {
        this.planPagos = planPagos;
    }
}