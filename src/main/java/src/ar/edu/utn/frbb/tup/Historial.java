package main.java.src.ar.edu.utn.frbb.tup;
import java.time.LocalDateTime;

public class Historial {
    private int id;
    private static LocalDateTime fechaYHora;
    private TipoDeOperacion tipodeoperacion;
    private double monto;

    //Constructor sin parametros
    public Historial(){};

    //Constructor con parametros
    public Historial(int id, TipoDeOperacion tipodeoperacion, double monto) {
        this.id = id;
        fechaYHora = LocalDateTime.now();
        this.tipodeoperacion = tipodeoperacion;
        this.monto = monto;
    }

    public String toString() {
        return "ID: " + id +
               "\nFecha y Hora: " + fechaYHora +
               "\nTipo de Operación: " + tipodeoperacion +
               "\nMonto: " + monto;
    }
    
    //getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    public static void setFechaYHora(LocalDateTime fechayhoraAhora) {
        fechaYHora = fechayhoraAhora;
    }

    public TipoDeOperacion getTipodeoperacion() {
        return tipodeoperacion;
    }

    public void setTipodeoperacion(TipoDeOperacion tipodeoperacion) {
        this.tipodeoperacion = tipodeoperacion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    };
}
