package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import ar.uba.fi.nicodiaz.mascota.utils.ParseProxyObject;

/**
 * Created by Juan Manuel Romera on 18/9/2015.
 */
@ParseClassName("Direccion")
public class Address extends ParseObject {

    private static final String CALLE_KEY = "calle";
    private static final String PISO_KEY = "piso";
    private static final String DEPARTAMENTO_KEY = "depto";
    private static final String LOCATION_KEY = "location";
    private static final String LOCALITY = "locality";
    private static final String SUBLOCALITY = "sublocality";

    public Address() {

    }

    public Address(ParseProxyObject ppo) {
        setCalle(ppo.getString(CALLE_KEY));
        setPiso(ppo.getString(PISO_KEY));
        setDepartamento(ppo.getString(DEPARTAMENTO_KEY));
        setLocality(ppo.getString(LOCALITY));
        setSubLocality((ppo.getString(SUBLOCALITY)));

        double latitude = ppo.getDouble(LOCATION_KEY + "-lat");
        double longitude = ppo.getDouble(LOCATION_KEY + "-long");
        setLocation(latitude, longitude);
    }


    public Address(String calle, double latitud, double longitud, String locality, String sublocality) {
        setCalle(calle);
        setLocation(latitud, longitud);
        setLocality(locality);
        setSubLocality(sublocality);
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

    public void setSubLocality(String subLocality) {
        if (subLocality != null) {
            put(SUBLOCALITY, subLocality);
        }
    }

    public String getSubLocality() {
        if (getString(SUBLOCALITY) == null || getString(SUBLOCALITY).equals(""))
            return getString(LOCALITY);
        return getString(SUBLOCALITY);
    }

    public void setLocality(String locality) {
        put(LOCALITY, locality);
    }

    public String getLocality() {
        return getString(LOCALITY);
    }

    public boolean hasSublocality() {
        if (getString(SUBLOCALITY) == null || getString(SUBLOCALITY).equals("")) {
            return false;
        }
        return true;
    }

}
