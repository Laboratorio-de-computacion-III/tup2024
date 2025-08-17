package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frbb.tup.controller.CuotaDto;

public class Prestamo {
    private Long id;
    private Long numeroCliente;
    private double montoPrestamo;
    private double montoConIntereses;
    private int plazoMeses;
    private TipoMoneda moneda;
    private LocalDate fechaSolicitud;
    private EstadoPrestamo estado;
    private int pagosRealizados;
    private double saldoRestante;
    private List<CuotaDto> planPagos;
    private static final double INTERES_ANUAL = 0.05; // 5% anual

    public Prestamo() {
        this.fechaSolicitud = LocalDate.now();
        this.pagosRealizados = 0;
        this.planPagos = new ArrayList<>();
        this.estado = EstadoPrestamo.PENDIENTE;
    }

    // Método para calcular el plan de pagos
    public void calcularPlanPagos() {
        double tasaMensual = INTERES_ANUAL / 12;
        double montoTotal = montoPrestamo * (1 + (INTERES_ANUAL * plazoMeses / 12));
        this.montoConIntereses = montoTotal;
        this.saldoRestante = montoTotal;

        double cuotaMensual = montoTotal / plazoMeses;

        for (int i = 1; i <= plazoMeses; i++) {
            CuotaDto cuota = new CuotaDto(i, cuotaMensual);
            planPagos.add(cuota);
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getNumeroCliente() { return numeroCliente; }
    public void setNumeroCliente(Long numeroCliente) { this.numeroCliente = numeroCliente; }

    public double getMontoPrestamo() { return montoPrestamo; }
    public void setMontoPrestamo(double montoPrestamo) { this.montoPrestamo = montoPrestamo; }

    public double getMontoConIntereses() { return montoConIntereses; }
    public void setMontoConIntereses(double montoConIntereses) { this.montoConIntereses = montoConIntereses; }

    public int getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(int plazoMeses) { this.plazoMeses = plazoMeses; }

    public TipoMoneda getMoneda() { return moneda; }
    public void setMoneda(TipoMoneda moneda) { this.moneda = moneda; }

    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDate fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public EstadoPrestamo getEstado() { return estado; }
    public void setEstado(EstadoPrestamo estado) { this.estado = estado; }

    public int getPagosRealizados() { return pagosRealizados; }
    public void setPagosRealizados(int pagosRealizados) { this.pagosRealizados = pagosRealizados; }

    public double getSaldoRestante() { return saldoRestante; }
    public void setSaldoRestante(double saldoRestante) { this.saldoRestante = saldoRestante; }

    public List<CuotaDto> getPlanPagos() { return planPagos; }
    public void setPlanPagos(List<CuotaDto> planPagos) { this.planPagos = planPagos; }
}
