package ar.edu.utn.frbb.tup.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public class ConsultaPrestamoResponseDto {

    private long numeroCliente;
    private List<PrestamoInfo> prestamos;

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public List<PrestamoInfo> getPrestamos() {
        return prestamos;
    }

    public static class PrestamoInfo {

        private final BigDecimal monto;
        private final int plazoMeses;
        private final int pagosRealizados;
        private final BigDecimal saldoRestante;

        public PrestamoInfo(BigDecimal monto, int plazoMeses, int pagosRealizados, BigDecimal saldoRestante) {
            this.monto = monto;
            this.plazoMeses = plazoMeses;
            this.pagosRealizados = pagosRealizados;
            this.saldoRestante = saldoRestante;
        }

        public BigDecimal getMonto() {
            return monto;
        }

        public int getPlazoMeses() {
            return plazoMeses;
        }

        public int getPagosRealizados() {
            return pagosRealizados;
        }

        public BigDecimal getSaldoRestante() {
            return saldoRestante;
        }
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public void setPrestamos(List<PrestamoInfo> prestamos) {
        this.prestamos = prestamos;
    }
}

