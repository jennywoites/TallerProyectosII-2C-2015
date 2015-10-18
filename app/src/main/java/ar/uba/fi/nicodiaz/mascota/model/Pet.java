package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public interface Pet {

    public final static char ADOPTION = 'A';
    public final static char MISSING = 'M';

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

    void setLocation(Address address);

    String getPreviewDescription();

    ParseFile getPicture();

    boolean isMale();

    List<ParseFile> getPictures();

    ArrayList<String> getVideos();

    char getType();

    String getID();

    Address getAddress();
}

