package ar.uba.fi.nicodiaz.mascota.model;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public interface Pet {

    public String getAgeRange();

    public String getDescription();

    public String getName();

    public String getGender();

    public String getKind();

    public User getOwner();

    public User getCatcher();

    public void setAgeRange(String ageRange);

    public void setDescription(String description);

    public void setName(String name);

    public void setGender(String gender);

    public void setKind(String petKind);

    public void setOwner(User user);

    public void setCatcher(User user);
}
