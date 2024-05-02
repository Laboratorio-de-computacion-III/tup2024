package ar.edu.utn.frbb.tup.utils;

import java.util.Arrays;
import java.util.Collections;

public class SimpleAdder {
    private static int sumaHistorica;

    Integer[] arreglo = new Integer[10];



    public SimpleAdder() {
    }

    public int add(int a, int b) {
        Collections.sort(Arrays.asList(arreglo));

        int resultado = a + b;
        this.setSumaHistorica(resultado);
        return resultado;
    }

    public int getSumaHistorica() {
        return sumaHistorica;
    }

    public void setSumaHistorica(int sumando) {
        this.sumaHistorica = sumaHistorica + sumando;
    }
}
