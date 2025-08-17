package ar.edu.utn.frbb.tup.persistence.entity;

import java.time.LocalDate;
import java.util.List;

import ar.edu.utn.frbb.tup.controller.CuotaDto;
import ar.edu.utn.frbb.tup.model.EstadoPrestamo;
import ar.edu.utn.frbb.tup.model.PlanPago;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoMoneda;

public class PrestamoEntity extends BaseEntity {
    private final long numeroCliente;
    private final double montoPrestamo;
    private final double montoConIntereses;
    private final int plazoMeses;
    private final String moneda;
    private final LocalDate fechaSolicitud;
    private final String estado;
    private final int pagosRealizados;
    private final double saldoRestante;
    private final List<CuotaDto> planPagos;

    public PrestamoEntity(Prestamo prestamo) {
        super(prestamo.getId());
        this.numeroCliente = prestamo.getNumeroCliente();
        this.montoPrestamo = prestamo.getMontoPrestamo();
        this.montoConIntereses = prestamo.getMontoConIntereses();
        this.plazoMeses = prestamo.getPlazoMeses();
        this.moneda = prestamo.getMoneda().toString();
        this.fechaSolicitud = prestamo.getFechaSolicitud();
        this.estado = prestamo.getEstado().toString();
        this.pagosRealizados = prestamo.getPagosRealizados();
        this.saldoRestante = prestamo.getSaldoRestante();
        this.planPagos = prestamo.getPlanPagos();
    }

    public Prestamo toPrestamo() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(this.getId());
        prestamo.setNumeroCliente(this.numeroCliente);
        prestamo.setMontoPrestamo(this.montoPrestamo);
        prestamo.setMontoConIntereses(this.montoConIntereses);
        prestamo.setPlazoMeses(this.plazoMeses);
        prestamo.setMoneda(TipoMoneda.valueOf(this.moneda));
        prestamo.setFechaSolicitud(this.fechaSolicitud);
        prestamo.setEstado(EstadoPrestamo.valueOf(this.estado));
        prestamo.setPagosRealizados(this.pagosRealizados);
        prestamo.setSaldoRestante(this.saldoRestante);
        prestamo.setPlanPagos(this.planPagos);
        return prestamo;
    }

    // Getters
    public long getNumeroCliente() { return numeroCliente; }
}

