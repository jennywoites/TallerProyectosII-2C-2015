package ar.uba.fi.nicodiaz.mascota.utils;

import java.util.ArrayList;

public class Filter {

    public ArrayList<Filter> children;
    public ArrayList<String> selection;


    public String name;

    private Filter() {
        children = new ArrayList<>();
        selection = new ArrayList<>();
    }

    public Filter(String name) {
        this();
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    private Filter addChildren(String child) {
        this.children.add(new Filter(child));
        return this;
    }

    public static ArrayList<Filter> getFilters() {
        ArrayList<Filter> filters = new ArrayList<>();

        filters.add(new Filter("Especie")
                            .addChildren("Perro")
                            .addChildren("Gato"));

        filters.add(new Filter("Sexo")
                        .addChildren("Macho")
                        .addChildren("Hembra"));

        // TODO: especificar rangos:
        filters.add(new Filter("Edad")
                        .addChildren("Cachorro")
                        .addChildren("Adulto"));

        // TODO: especificar rangos:
        filters.add(new Filter("Distancia")
                        .addChildren("Menos de 1km")
                        .addChildren("Entre 1km y 5km")
                        .addChildren("Entre 5km y 10km")
                        .addChildren("Entre 10km y 15km")
                        .addChildren("Más de 15km"));

        return filters;
    }

 /*   public static Filter get(String name)
    {
        ArrayList<Filter> collection = Filter.getFilters();
        for (Filter cat : collection) {
            if(cat.name.equals(name)) {
                return cat;
            }
        }
        return null;
    }*/
}
