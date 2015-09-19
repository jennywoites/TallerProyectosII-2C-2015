package ar.uba.fi.nicodiaz.mascota.model;

/**
 * Created by Juan Manuel Romera on 18/9/2015.
 */
public class Address {

    String calle;
    String piso;
    String departamento;
    double latitud;
    double longitud;

    public Address(String calle, double longitud, double latitud) {
        this.calle = calle;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public Address(String calle, String piso, String departamento, double longitud, double latitud) {
        this.calle = calle;
        this.piso = piso;
        this.departamento = departamento;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
