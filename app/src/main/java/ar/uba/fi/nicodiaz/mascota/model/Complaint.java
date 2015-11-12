package ar.uba.fi.nicodiaz.mascota.model;

/**
 * Created by Juan Manuel Romera on 11/11/2015.
 */
public interface Complaint {

    public void setInformed(User user);

    public User getInformed();

    public void setInformer(User user);

    public User getInformer();

    public void setInfo(String info);

    public String getInfo();

    public void setPublication(Pet pet);

    public Pet getPublication();

    public void setKind(String kind);

    public String getKind();

}
