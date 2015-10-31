package ar.uba.fi.nicodiaz.mascota.model.service;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.model.Address;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPetState;
import ar.uba.fi.nicodiaz.mascota.model.FoundPetState;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.User;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
@ParseClassName("MascotaEncontrada")
public class FoundPet extends ParseObject implements Pet {

    public static final String OBJECT_ID = "objectId";
    public static final String AGE = "age";
    public static final String DESCRIPTION = "description";
    public static final String GENDER = "gender";
    public static final String KIND = "kind";
    public static final String BREED = "breed";
    public static final String PUBLISHER = "publisher";
    public static final String OWNER = "owner";
    public static final String VIDEO_ONE = "urlOne";
    public static final String VIDEO_TWO = "urlTwo";
    public static final String VIDEO_THREE = "urlThree";
    public static final String PHOTO_ONE = "picture";
    public static final String PHOTO_TWO = "picture2";
    public static final String PHOTO_THREE = "picture3";
    public static final String PHOTO_FOUR = "picture4";
    public static final String PHOTO_FIVE = "picture5";
    public static final String LOCATION = "location";
    public static final String STATE = "state";
    public static final String LAST_KNOW_DATE = "lastKnowDate";
    public static final String LAST_KNOW_ADDRESS = "lastKnowAddress";
    public static String MALE = "Macho";

    public FoundPet() {
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
        throw new NoSuchMethodError();
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
        return null;
    }

    public void setPublisher(User user) {
        put(PUBLISHER, user.getParseUser());
    }


    @Override
    public User getPublisher() {
        ParseUser parseUser = getParseUser(PUBLISHER);
        return new User(parseUser);
    }

    @Override
    public String getOtherPets() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getChildren() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getSocialNotes() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getMedicine() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getMedicineTime() {
        throw new NoSuchMethodError();
    }

    @Override
    public String getMedicineNotes() {
        throw new NoSuchMethodError();
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
        throw new NoSuchMethodError();
    }

    @Override
    public void setOtherPets(String otherPets) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setChildren(String children) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setSocialNotes(String notes) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setMedicine(String medicine) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setMedicineTime(String time) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setMedicineNotes(String notes) {
        throw new NoSuchMethodError();
    }

    @Override
    public void setBreed(String breed) {
        put(BREED, breed);
    }

    @Override
    public void setLocation(Address address) {
        put(LOCATION, address.getLocation());
    }

    public Address getAddress() {
        Address address = getLastKnowAddress();
        return address;
    }

    @Override
    public String getPreviewDescription() {
        String previewDescription = "";
        previewDescription += "Edad: " + getAgeRange();
        Address address = this.getLastKnowAddress();

        if (address != null) {
            if (address.getSubLocality() != null && !address.getSubLocality().isEmpty()) {
                previewDescription += " / Ubicación: " + address.getSubLocality();
            } else if (address.getLocality() != null && !address.getLocality().isEmpty()) {
                previewDescription += " / Ubicación: " + address.getLocality();
            }
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
        return Pet.FOUND;
    }

    @Override
    public String getID() {
        return getObjectId();
    }

    public void setState(FoundPetState foundPetState) {
        put(STATE, foundPetState.toString());
    }

    public FoundPetState getState() {
        String text = getString(STATE);
        return FoundPetState.getState(text);
    }

    public void setLastKnowDate(Date lastKnowDate) {
        put(LAST_KNOW_DATE, lastKnowDate);
    }

    public Date getLastKnowDate() {
        return getDate(LAST_KNOW_DATE);
    }

    public void setLastKnowAddress(Address lastKnowAddress) {
        setLocation(lastKnowAddress);
        put(LAST_KNOW_ADDRESS, lastKnowAddress);
    }

    public Address getLastKnowAddress() {
        try {
            return (Address) getParseObject(LAST_KNOW_ADDRESS).fetchIfNeeded();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}