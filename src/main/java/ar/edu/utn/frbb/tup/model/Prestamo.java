package ar.edu.utn.frbb.tup.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private BigDecimal montoPrestamo;

    @Column(name = "monto_con_intereses", nullable = false)
    private BigDecimal montoConIntereses;

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
    private BigDecimal saldoRestante;

    @Transient
    private List<CuotaDto> planPagos;

    private static final double INTERES_ANUAL = 0.05; // 5% anual

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

        double anios = plazoMeses / 12.0;
        BigDecimal montoTotal = montoPrestamo.multiply(BigDecimal.valueOf(1 + (INTERES_ANUAL * anios)));

        this.montoConIntereses = montoTotal;
        this.saldoRestante = montoTotal;

        BigDecimal cuotaMensual = montoTotal.divide(new BigDecimal(plazoMeses), 2, RoundingMode.HALF_UP);

        this.planPagos = new ArrayList<>();
        for (int i = 1; i <= plazoMeses; i++) {
            CuotaDto cuota = new CuotaDto(i, cuotaMensual);
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


    public void setNumeroCliente(Long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public BigDecimal getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(BigDecimal montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
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


    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public Integer getPagosRealizados() {
        return pagosRealizados;
    }


    public BigDecimal getSaldoRestante() {
        return saldoRestante;
    }


    public List<CuotaDto> getPlanPagos() {
        if (planPagos == null || planPagos.isEmpty()) {
            calcularPlanPagos();
        }
        return planPagos;
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