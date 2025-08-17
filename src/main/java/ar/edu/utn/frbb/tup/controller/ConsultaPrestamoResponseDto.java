package ar.edu.utn.frbb.tup.controller;

import java.util.List;

public class ConsultaPrestamoResponseDto {
    private long numeroCliente;
    private List<PrestamoInfo> prestamos;

    public static class PrestamoInfo {
        private double monto;
        private int plazoMeses;
        private int pagosRealizados;
        private double saldoRestante;

        // Constructor
        public PrestamoInfo(double monto, int plazoMeses, int pagosRealizados, double saldoRestante) {
            this.monto = monto;
            this.plazoMeses = plazoMeses;
            this.pagosRealizados = pagosRealizados;
            this.saldoRestante = saldoRestante;
        }

        // Getters y Setters
        public double getMonto() { return monto; }
        public void setMonto(double monto) { this.monto = monto; }

        public int getPlazoMeses() { return plazoMeses; }
        public void setPlazoMeses(int plazoMeses) { this.plazoMeses = plazoMeses; }

        public int getPagosRealizados() { return pagosRealizados; }
        public void setPagosRealizados(int pagosRealizados) { this.pagosRealizados = pagosRealizados; }

        public double getSaldoRestante() { return saldoRestante; }
        public void setSaldoRestante(double saldoRestante) { this.saldoRestante = saldoRestante; }
    }

    public long getNumeroCliente() { return numeroCliente; }
    public void setNumeroCliente(long numeroCliente) { this.numeroCliente = numeroCliente; }

    public List<PrestamoInfo> getPrestamos() { return prestamos; }
    public void setPrestamos(List<PrestamoInfo> prestamos) { this.prestamos = prestamos; }
}

