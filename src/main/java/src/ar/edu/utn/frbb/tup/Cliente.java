package main.java.src.ar.edu.utn.frbb.tup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Banco{
    private String dni; //ID UNICO
    private String nombre;
    private String apellido;
    private String direccion;
    private String tel;
    private LocalDate fechaInicio;
    protected static List<CuentasBancarias> cuentas = new ArrayList<>();
    
    public Cliente(String dni, String nombre, String apellido, String direccion, String tel) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.tel = tel;
    }

    public Cliente() {}; //Constructor vacio

    public String toString() {  
        return "Cliente{" +
                "dni='" + dni + '\'' +
                ", fecha de nacimiento='" + fechaInicio + '\'' + 
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", direccion='" + direccion + '\'' +
                ", tel=" + tel +
                '}';
    }


    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }   
    
}
