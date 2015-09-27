package ar.uba.fi.nicodiaz.mascota.utils;

import java.util.ArrayList;

import ar.uba.fi.nicodiaz.mascota.MyApplication;
import ar.uba.fi.nicodiaz.mascota.R;

public class Filter {

    public ArrayList<Filter> children;
    public ArrayList<String> selection;

    public static String ESPECIE = "Especie";
    public static String SEXO = "Sexo";
    public static String EDAD = "Edad";
    public static String DISTANCIA = "Distancia";
    public static String MAS_15 = "Más de 15km";
    public static String MENOS_1 = "Menos de 1km";


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


        filters.add(new Filter(ESPECIE)
                .addChildren(MyApplication.getContext().getResources().getString(R.string.dog))
                .addChildren(MyApplication.getContext().getResources().getString(R.string.cat)));

        filters.add(new Filter(SEXO)
                .addChildren(MyApplication.getContext().getResources().getString(R.string.pet_male))
                .addChildren(MyApplication.getContext().getResources().getString(R.string.pet_female)));

        // TODO: especificar rangos:
        filters.add(new Filter(EDAD)
                .addChildren(MyApplication.getContext().getResources().getString(R.string._0_3_months))
                .addChildren(MyApplication.getContext().getResources().getString(R.string._4_6_months))
                .addChildren(MyApplication.getContext().getResources().getString(R.string._7_12_months))
                .addChildren(MyApplication.getContext().getResources().getString(R.string._1_3_years))
                .addChildren(MyApplication.getContext().getResources().getString(R.string._4_7_years))
                .addChildren(MyApplication.getContext().getResources().getString(R.string._8_15_years)));

        // TODO: especificar rangos:
        filters.add(new Filter(DISTANCIA)
                .addChildren(MENOS_1)
                .addChildren("Entre 1km y 5km")
                .addChildren("Entre 5km y 10km")
                .addChildren("Entre 10km y 15km")
                .addChildren(MAS_15));

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
