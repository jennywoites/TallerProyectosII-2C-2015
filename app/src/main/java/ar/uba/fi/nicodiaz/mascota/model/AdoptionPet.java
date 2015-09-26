package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.utils.ParseProxyObject;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
@ParseClassName("MascotaEnAdopcion")
public class AdoptionPet extends ParseObject implements Pet {

    public static final String AGE = "age";
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String KIND = "kind";
    public static final String BREED = "breed";
    public static final String OWNER = "owner";
    public static final String CATCHER = "catcher";
    public static final String PICTURE = "picture";
    public static final String PETS = "pets";
    public static final String CHILDREN = "children";
    public static final String SOCIAL_NOTES = "socialNotes";
    public static final String MEDICINE = "medicine";
    public static final String MEDICINE_TIME = "medicineTime";
    public static final String MEDICINE_NOTES = "medicineNotes";
    public static final String LOCATION = "location";
    public static String MALE = "Macho";

    private AdoptionPet instance = this;

    public AdoptionPet() {
    }

    public AdoptionPet(ParseProxyObject ppo) {
        setAgeRange(ppo.getString(AGE));
        setDescription(ppo.getString(DESCRIPTION));
        setName(ppo.getString(NAME));
        setGender(ppo.getString(GENDER));
        setKind(ppo.getString(KIND));
        setBreed(ppo.getString(BREED));
/*        User owner = new User(ppo.getParseUser(OWNER));
        setOwner(owner);*/
        setPicture(new ParseFile(ppo.getBytes(PICTURE)));
        setOtherPets(ppo.getString(PETS));
        setChildren(ppo.getString(CHILDREN));
        setSocialNotes(ppo.getString(SOCIAL_NOTES));
        setMedicine(ppo.getString(MEDICINE));
        setMedicineTime(ppo.getString(MEDICINE_TIME));
        setMedicineNotes(ppo.getString(MEDICINE_NOTES));
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
    public String getBreed() {
        return getString(BREED);
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
        ParseGeoPoint location = user.getAddress().getLocation();
        put(LOCATION, location);
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

    @Override
    public void setBreed(String breed) {
        put(BREED, breed);
    }

    @Override
    public String getPreviewDescription() {
        String previewDescription = "";

        previewDescription += "Edad: " + getAgeRange();
        previewDescription += " / Ubicación: " + "STUB"; // TODO: incluir barrio (?)

        return previewDescription;
    }

    public void setPicture(ParseFile file) {
        put(PICTURE, file);
    }

    public ParseFile getPicture() {
        return getParseFile(PICTURE);
    }

    public boolean isMale() {
        return getGender().equals(MALE);
    }

}
