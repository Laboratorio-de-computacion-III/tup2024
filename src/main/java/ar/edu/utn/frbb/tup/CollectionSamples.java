package ar.edu.utn.frbb.tup;

import java.util.*;

public class CollectionSamples {

    public void runSample() {
        System.out.println("Laboratorio de Computación III - 2024");
        System.out.println("---------------------------------------");
        System.out.println("Collection Framework");
        System.out.println("---------------------------------------");
        System.out.println("Set");
        System.out.println("---------------------------------------");

        Set<String> setNombres = new HashSet<>();

        System.out.println(setNombres);

        setNombres.add("Luciano");
        setNombres.add("Andoni");
        setNombres.add("Sofia");
        setNombres.add("Laura");

        System.out.println("Nombres desordenados: " + setNombres);

        setNombres = new LinkedHashSet<>();
        setNombres.add("Luciano");
        setNombres.add("Andoni");
        setNombres.add("Sofia");
        setNombres.add("Laura");

        System.out.println("Nombres: " + setNombres);

        for(String persona : setNombres) {
            System.out.println(persona);
        }

        System.out.println("---------------------------------------");
        System.out.println("List");

        List<String> listaNombres = new ArrayList<>();

        listaNombres.add("Luciano");
        listaNombres.add("Andoni");
        listaNombres.add("Sofia");
        listaNombres.add("Laura");
        listaNombres.add("Luciano");

        System.out.println("Lista de nombres: " + listaNombres);
        System.out.println("Tercer persona en la lista: " + listaNombres.get(2));

        for(int i = 0; i < listaNombres.size(); i++) {
            System.out.println(i + " - " + listaNombres.get(i));
        }



        System.out.println("------------------------------------------");

        System.out.println("Map");

        Map<String, Integer> alumnosNotas = new HashMap<>();

        alumnosNotas.put("Andoni", 10);
        alumnosNotas.put("Luciano", 10);
        alumnosNotas.put("Sofia", 7);
        alumnosNotas.put("Sofia", 9);

        System.out.println(alumnosNotas);


        System.out.println("");


    }

}
