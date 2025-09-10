package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.CuotaDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_cliente", nullable = false)
    private Long numeroCliente;

    @Column(name = "monto_prestamo", nullable = false)
    private Double montoPrestamo;

    @Column(name = "monto_con_intereses", nullable = false)
    private Double montoConIntereses;

    @Column(name = "plazo_meses", nullable = false)
    private Integer plazoMeses;

    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false, length = 10)
    private TipoMoneda moneda;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPrestamo estado;

    @Column(name = "pagos_realizados", nullable = false)
    private Integer pagosRealizados;

    @Column(name = "saldo_restante", nullable = false)
    private Double saldoRestante;

    // Plan de pagos almacenado como JSON o como entidades separadas
    // Por simplicidad, lo mantenemos transitorio y se calcula dinámicamente
    @Transient
    private List<CuotaDto> planPagos;

    // Constante para el interés
    private static final double INTERES_ANUAL = 0.05; // 5% anual

    // Constructores
    public Prestamo() {
        this.fechaSolicitud = LocalDate.now();
        this.pagosRealizados = 0;
        this.planPagos = new ArrayList<>();
        this.estado = EstadoPrestamo.PENDIENTE;
    }

    /**
     * Calcula el plan de pagos con interés fijo del 5% anual
     * Todas las cuotas tienen el mismo valor
     */
    public void calcularPlanPagos() {
        // Calcular el interés total
        double tasaAnual = INTERES_ANUAL;
        double anios = plazoMeses / 12.0;
        double montoTotal = montoPrestamo * (1 + (tasaAnual * anios));

        this.montoConIntereses = montoTotal;
        this.saldoRestante = montoTotal;

        // Calcular cuota mensual (todas iguales)
        double cuotaMensual = montoTotal / plazoMeses;

        // Generar plan de pagos
        this.planPagos = new ArrayList<>();
        for (int i = 1; i <= plazoMeses; i++) {
            CuotaDto cuota = new CuotaDto(i, Math.round(cuotaMensual * 100.0) / 100.0);
            planPagos.add(cuota);
        }
    }

    /**
     * Método que se ejecuta después de cargar la entidad para recalcular el plan de pagos
     */
    @PostLoad
    public void postLoad() {
        if (planPagos == null || planPagos.isEmpty()) {
            calcularPlanPagos();
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(Long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public Double getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(Double montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public Double getMontoConIntereses() {
        return montoConIntereses;
    }

    public void setMontoConIntereses(Double montoConIntereses) {
        this.montoConIntereses = montoConIntereses;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public Integer getPagosRealizados() {
        return pagosRealizados;
    }

    public void setPagosRealizados(Integer pagosRealizados) {
        this.pagosRealizados = pagosRealizados;
    }

    public Double getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(Double saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public List<CuotaDto> getPlanPagos() {
        if (planPagos == null || planPagos.isEmpty()) {
            calcularPlanPagos();
        }
        return planPagos;
    }

    public void setPlanPagos(List<CuotaDto> planPagos) {
        this.planPagos = planPagos;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", numeroCliente=" + numeroCliente +
                ", montoPrestamo=" + montoPrestamo +
                ", montoConIntereses=" + montoConIntereses +
                ", plazoMeses=" + plazoMeses +
                ", moneda=" + moneda +
                ", fechaSolicitud=" + fechaSolicitud +
                ", estado=" + estado +
                ", pagosRealizados=" + pagosRealizados +
                ", saldoRestante=" + saldoRestante +
                '}';
    }
}