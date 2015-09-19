package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by Juan Manuel Romera on 18/9/2015.
 */
@ParseClassName("Direccion")
public class Address extends ParseObject {

    private static final String CALLE_KEY = "calle";
    private static final String PISO_KEY = "piso";
    private static final String DEPARTAMENTO_KEY = "depto";
    private static final String LOCATION_KEY = "location";

    public Address() {

    }

    public Address(String calle, double latitud, double longitud) {
        setCalle(calle);
        setLocation(latitud, longitud);
    }


    public String getCalle() {
        return getString(CALLE_KEY);
    }

    public void setCalle(String calle) {
        put(CALLE_KEY, calle);
    }

    public String getPiso() {
        return getString(PISO_KEY);
    }

    public void setPiso(String piso) {
        put(PISO_KEY, piso);
    }

    public String getDepartamento() {
        return getString(DEPARTAMENTO_KEY);
    }

    public void setDepartamento(String departamento) {
        put(DEPARTAMENTO_KEY, departamento);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(LOCATION_KEY);
    }

    public void setLocation(double latitud, double longitud) {
        put(LOCATION_KEY, new ParseGeoPoint(latitud, longitud));
    }


}
