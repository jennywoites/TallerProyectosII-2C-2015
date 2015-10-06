package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseFile;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public interface Pet {

    String getAgeRange();

    String getDescription();

    String getName();

    String getGender();

    String getKind();

    User getOwner();

    User getCatcher();

    String getOtherPets();

    String getChildren();

    String getSocialNotes();

    String getMedicine();

    String getMedicineTime();

    String getMedicineNotes();

    String getBreed();

    void setAgeRange(String ageRange);

    void setDescription(String description);

    void setName(String name);

    void setGender(String gender);

    void setKind(String petKind);

    void setOwner(User user);

    void setCatcher(User user);

    void setOtherPets(String otherPets);

    void setChildren(String children);

    void setSocialNotes(String notes);

    void setMedicine(String medicine);

    void setMedicineTime(String time);

    void setMedicineNotes(String notes);

    void setBreed(String breed);

    String getPreviewDescription();

    ParseFile getPicture();

    boolean isMale();
}

