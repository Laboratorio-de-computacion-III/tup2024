package ar.edu.utn.frbb.tup.utils;

import java.time.LocalDateTime;

public class Cuenta {
	
	// Enumeración para representar el tipo de cuenta
    public enum TipoCuenta {
        CAJA_AHORRO,
        CUENTA_CORRIENTE
    }
    
    // Atributos de Cuenta.
    private String numeroCuenta;
    private LocalDateTime fechaCreacion;
    private double saldo;
    private TipoCuenta tipoCuenta;
	private Cliente cliente;
    

    // Constructor
	public Cuenta(String numeroCuenta, double saldo, TipoCuenta tipoCuenta, Cliente cliente) {
	    this.numeroCuenta = numeroCuenta;
	    this.fechaCreacion = LocalDateTime.now();
	    this.saldo = saldo;
	    this.tipoCuenta = tipoCuenta;
	    this.cliente = cliente;
	}

	// Métodos getter y setter
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    
 // Método getter para obtener el cliente asociado
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public void depositar(double monto) {
        this.saldo += monto;
    }

    // Método para retirar dinero de la cuenta
    public boolean retirar(double monto) {
        if (monto <= this.saldo) {
            this.saldo -= monto;
            return true; // Retiro exitoso
        } else {
            return false; // Saldo insuficiente
        }
    }
}
