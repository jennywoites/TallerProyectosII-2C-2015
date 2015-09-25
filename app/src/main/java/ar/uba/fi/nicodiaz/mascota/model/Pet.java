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

    public String getOtherPets();

    public String getChildren();

    public String getSocialNotes();

    public String getMedicine();

    public String getMedicineTime();

    public String getMedicineNotes();

    public void setAgeRange(String ageRange);

    public void setDescription(String description);

    public void setName(String name);

    public void setGender(String gender);

    public void setKind(String petKind);

    public void setOwner(User user);

    public void setCatcher(User user);

    public void setOtherPets(String otherPets);

    public void setChildren(String children);

    public void setSocialNotes(String notes);

    public void setMedicine(String medicine);

    public void setMedicineTime(String time);

    public void setMedicineNotes(String notes);
}
