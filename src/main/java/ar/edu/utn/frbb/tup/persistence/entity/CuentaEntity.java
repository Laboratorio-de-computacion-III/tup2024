package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cuenta")
public class CuentaEntity extends BaseEntity{
    @Column(name = "nombre")
    String nombre;

    @Column(name = "fecha_creacion")
    LocalDateTime fechaCreacion;

    @Column(name = "balance")
    int balance;

    @Column(name = "tipo_cuenta")
    String tipoCuenta;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "titular_dni")
    ClienteEntity titular;

    // Convenience mirror of id for legacy mapping
    @Transient
    long numeroCuenta;

    public CuentaEntity() {}

    public CuentaEntity(Cuenta cuenta) {
        super(cuenta.getNumeroCuenta());
        this.numeroCuenta = cuenta.getNumeroCuenta();
        this.balance = cuenta.getBalance();
        this.tipoCuenta = cuenta.getTipoCuenta().toString();
        this.fechaCreacion = cuenta.getFechaCreacion();
    }

    public Cuenta toCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(this.balance);
        cuenta.setNumeroCuenta(this.getId());
        cuenta.setTipoCuenta(TipoCuenta.valueOf(this.tipoCuenta));
        cuenta.setFechaCreacion(this.fechaCreacion);
        return cuenta;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }
    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public ClienteEntity getTitular() { return titular; }
    public void setTitular(ClienteEntity titular) { this.titular = titular; }
    public long getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(long numeroCuenta) { this.numeroCuenta = numeroCuenta; }
}
