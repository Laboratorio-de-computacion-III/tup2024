package ar.edu.utn.frbb.tup.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public class ConsultaPrestamoResponseDto {

    private long numeroCliente;
    private List<PrestamoInfo> prestamos;

    public static class PrestamoInfo {

        private BigDecimal monto;
        private int plazoMeses;
        private int pagosRealizados;
        private BigDecimal saldoRestante;

        public PrestamoInfo(BigDecimal monto, int plazoMeses, int pagosRealizados, BigDecimal saldoRestante) {
            this.monto = monto;
            this.plazoMeses = plazoMeses;
            this.pagosRealizados = pagosRealizados;
            this.saldoRestante = saldoRestante;
        }

    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public void setPrestamos(List<PrestamoInfo> prestamos) {
        this.prestamos = prestamos;
    }
}

