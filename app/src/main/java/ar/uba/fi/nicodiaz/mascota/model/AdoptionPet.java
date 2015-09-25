package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
@ParseClassName("MascotaEnAdopcion")
public class AdoptionPet extends ParseObject implements Pet {

    private static final String AGE = "age";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String GENDER = "gender";
    private static final String KIND = "kind";
    private static final String OWNER = "owner";
    private static final String CATCHER = "catcher";
    private static final String PICTURE = "picture";
    private static final String PETS = "pets";
    private static final String CHILDREN = "children";
    private static final String SOCIAL_NOTES = "socialNotes";
    private static final String MEDICINE = "medicine";
    private static final String MEDICINE_TIME = "medicineTime";
    private static final String MEDICINE_NOTES = "medicineNotes";

    private AdoptionPet instance = this;

    public AdoptionPet() {
    }

    @Override
    public String getAgeRange() {
        return getString(AGE);
    }

    @Override
    public String getDescription() {
        return getString(DESCRIPTION);
    }

    @Override
    public String getName() {
        return getString(NAME);
    }

    @Override
    public String getGender() {
        return getString(GENDER);
    }

    @Override
    public String getKind() {
        return getString(KIND);
    }

    @Override
    public User getOwner() {
        ParseUser parseUser = getParseUser(OWNER);
        return new User(parseUser);
    }

    @Override
    public User getCatcher() {
        ParseUser parseUser = getParseUser(CATCHER);
        return new User(parseUser);
    }

    @Override
    public String getOtherPets() {
        return getString(PETS);
    }

    @Override
    public String getChildren() {
        return getString(CHILDREN);
    }

    @Override
    public String getSocialNotes() {
        return getString(SOCIAL_NOTES);
    }

    @Override
    public String getMedicine() {
        return getString(MEDICINE);
    }

    @Override
    public String getMedicineTime() {
        return getString(MEDICINE_TIME);
    }

    @Override
    public String getMedicineNotes() {
        return getString(MEDICINE_NOTES);
    }

    @Override
    public void setAgeRange(String ageRange) {
        put(AGE, ageRange);
    }

    @Override
    public void setDescription(String description) {
        put(DESCRIPTION, description);
    }

    @Override
    public void setName(String name) {
        put(NAME, name);
    }

    @Override
    public void setGender(String gender) {
        put(GENDER, gender);
    }

    @Override
    public void setKind(String petKind) {
        put(KIND, petKind);
    }

    @Override
    public void setOwner(User user) {
        put(OWNER, user.getParseUser());
    }

    @Override
    public void setCatcher(User user) {
        put(CATCHER, user.getParseUser());
    }

    @Override
    public void setOtherPets(String otherPets) {
        put(PETS, otherPets);
    }

    @Override
    public void setChildren(String children) {
        put(CHILDREN, children);
    }

    @Override
    public void setSocialNotes(String notes) {
        put(SOCIAL_NOTES, notes);
    }

    @Override
    public void setMedicine(String medicine) {
        put(MEDICINE, medicine);
    }

    @Override
    public void setMedicineTime(String time) {
        put(MEDICINE_TIME, time);
    }

    @Override
    public void setMedicineNotes(String notes) {
        put(MEDICINE_NOTES, notes);
    }

    public void setPicture(ParseFile file) {
        put(PICTURE, file);
    }

    public ParseFile getPicture() {
        return getParseFile(PICTURE);
    }
}
