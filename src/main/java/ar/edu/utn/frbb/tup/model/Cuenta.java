package ar.edu.utn.frbb.tup.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import com.fasterxml.jackson.annotation.JsonBackReference;

import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    @Column(name = "numero_cuenta", nullable = false, unique = true)
    private Long numeroCuenta;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private TipoCuenta tipoCuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false, length = 10)
    private TipoMoneda moneda;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dni_titular", nullable = false)
    private Cliente titular;

    public Cuenta() {
        this.numeroCuenta = Math.abs(new Random().nextLong());
        this.balance = BigDecimal.ZERO;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getNumeroCuenta() {
        return numeroCuenta;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public Cuenta setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    // Métodos de negocio
    public void debitarDeCuenta(BigDecimal cantidadADebitar) throws NoAlcanzaException, CantidadNegativaException {
        if (cantidadADebitar.compareTo(BigDecimal.ZERO) < 0) {
            throw new CantidadNegativaException();
        }

        if (this.balance.compareTo(cantidadADebitar) < 0) {
            throw new NoAlcanzaException();
        }
        this.balance = this.balance.subtract(cantidadADebitar);
    }

    public void forzaDebitoDeCuenta(BigDecimal i) {
        this.balance = this.balance.subtract(i);
    }

    @Override
    public String toString() {
        return "Cuenta{" + "numeroCuenta=" + numeroCuenta + ", fechaCreacion=" + fechaCreacion + ", balance=" + balance
                + ", tipoCuenta=" + tipoCuenta + ", moneda=" + moneda + ", titular=" + (titular != null
                ? titular.getDni() : "null") + '}';
    }
}