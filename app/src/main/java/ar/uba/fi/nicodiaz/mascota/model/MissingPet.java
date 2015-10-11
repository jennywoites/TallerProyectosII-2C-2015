package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.utils.ParseProxyObject;

/**
 * Created by Juan Manuel Romera on 6/10/2015.
 */
@ParseClassName("MascotaPerdida")
public class MissingPet extends ParseObject implements Pet {

    public static final String AGE = "age";
    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String KIND = "kind";
    public static final String BREED = "breed";
    public static final String OWNER = "owner";
    public static final String CATCHER = "catcher";
    public static final String PETS = "pets";
    public static final String CHILDREN = "children";
    public static final String SOCIAL_NOTES = "socialNotes";
    public static final String MEDICINE = "medicine";
    public static final String MEDICINE_TIME = "medicineTime";
    public static final String MEDICINE_NOTES = "medicineNotes";
    public static final String VIDEO_ONE = "urlOne";
    public static final String VIDEO_TWO = "urlTwo";
    public static final String VIDEO_THREE = "urlThree";
    public static final String PHOTO_ONE = "picture";
    public static final String PHOTO_TWO = "picture2";
    public static final String PHOTO_THREE = "picture3";
    public static final String PHOTO_FOUR = "picture4";
    public static final String PHOTO_FIVE = "picture5";
    public static final String LOCATION = "location";
    public static String MALE = "Macho";

    public MissingPet() {
    }

    public MissingPet(ParseProxyObject ppo) {
        setAgeRange(ppo.getString(AGE));
        setDescription(ppo.getString(DESCRIPTION));
        setName(ppo.getString(NAME));
        setGender(ppo.getString(GENDER));
        setKind(ppo.getString(KIND));
        setBreed(ppo.getString(BREED));
        User owner = new User(ppo.getParseObject(OWNER));
        setOwner(owner);
        setPicture(new ParseFile(ppo.getBytes(PHOTO_ONE)));
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
        try {
            return new User(parseUser.fetchIfNeeded());
        } catch (ParseException e) {
            return null;
        }
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

    public Address getAddress() {
        Address address = getOwner().getAddress();
        return address;
    }

    @Override
    public String getPreviewDescription() {
        String previewDescription = "";

        previewDescription += "Edad: " + getAgeRange();

        Address address = getOwner().getAddress();
        if (address.getSubLocality() != null && !address.getSubLocality().isEmpty()) {
            previewDescription += " / Ubicación: " + address.getSubLocality();
        } else if (address.getLocality() != null && !address.getLocality().isEmpty()) {
            previewDescription += " / Ubicación: " + address.getLocality();
        }

        return previewDescription;
    }

    public void setPicture(ParseFile file) {
        if (get(PHOTO_ONE) == null) {
            put(PHOTO_ONE, file);
        } else if (get(PHOTO_TWO) == null) {
            put(PHOTO_TWO, file);
        } else if (get(PHOTO_THREE) == null) {
            put(PHOTO_THREE, file);
        } else if (get(PHOTO_FOUR) == null) {
            put(PHOTO_FOUR, file);
        } else if (get(PHOTO_FIVE) == null) {
            put(PHOTO_FIVE, file);
        }
    }

    public ParseFile getPicture() {
        return getParseFile(PHOTO_ONE);
    }

    public boolean isMale() {
        return getGender().equals(MALE);
    }

    public List<ParseFile> getPictures() {
        List<ParseFile> list = new ArrayList<>();
        if (get(PHOTO_ONE) != null) {
            list.add(getParseFile(PHOTO_ONE));
        }
        if (get(PHOTO_TWO) != null) {
            list.add(getParseFile(PHOTO_TWO));
        }
        if (get(PHOTO_THREE) != null) {
            list.add(getParseFile(PHOTO_THREE));
        }
        if (get(PHOTO_FOUR) != null) {
            list.add(getParseFile(PHOTO_FOUR));
        }
        if (get(PHOTO_FIVE) != null) {
            list.add(getParseFile(PHOTO_FIVE));
        }
        return list;
    }

    public void setVideo1(String video1) {
        put(VIDEO_ONE, video1);
    }

    public void setVideo2(String video2) {
        put(VIDEO_TWO, video2);
    }

    public void setVideo3(String video3) {
        put(VIDEO_THREE, video3);
    }

    public ArrayList<String> getVideos() {
        ArrayList<String> list = new ArrayList<>();
        String video1 = getString(VIDEO_ONE);
        if (video1 != null && !video1.isEmpty()) {
            list.add(video1);
        }
        String video2 = getString(VIDEO_TWO);
        if (video2 != null && !video2.isEmpty()) {
            list.add(video2);
        }
        String video3 = getString(VIDEO_THREE);
        if (video3 != null && !video3.isEmpty()) {
            list.add(video3);
        }
        return list;
    }

    @Override
    public char getType() {
        return Pet.MISSING;
    }

    @Override
    public String getID() {
        return getObjectId();
    }
}
