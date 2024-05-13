package src.ar.edu.utn.frbb.tup;
import java.time.LocalDate;


public class CuentasBancarias extends Cliente{
    private int id;
    private TipoCuenta tipocuenta;
    private double saldo;
    private Cliente cliente;
    private LocalDate fechaCreacion;



    public CuentasBancarias(Cliente cliente) {
        this.cliente = cliente;
    }

    public CuentasBancarias() {};
    
    public String toString() {
        return "Cuenta Bancaria {" +
                "ID: " + id +
                ", Tipo de cuenta: " + tipocuenta +
                ", Saldo: $" + saldo +
                ", Cliente: " + cliente +
                ", Fecha de Creación: " + fechaCreacion +
                '}';
    }

    public static CuentasBancarias buscarCuentaxID(int id) {
        for (CuentasBancarias cuenta : cuentas) {
            if (cuenta.getId() == id){
                return cuenta;
            }
        }
        return null; 
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoCuenta getTipocuenta() {
        return tipocuenta;
    }

    public void setTipocuenta(TipoCuenta tipocuenta) {
        this.tipocuenta = tipocuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
