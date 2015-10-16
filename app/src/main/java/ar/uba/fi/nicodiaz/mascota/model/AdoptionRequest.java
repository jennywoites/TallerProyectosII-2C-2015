package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by JFERRIO on 13/10/2015.
 */
@ParseClassName("SolicitudMascotaEnAdopcion")
public class AdoptionRequest extends ParseObject {

    public static final String REQUESTING_USER = "requestingUser";
    public static final String ADOPTION_PET = "pet";
    public static final String MESSAGE = "message";
    public static final String STATE = "state";
    public static final String DATE = "date";

    public AdoptionRequest() {

    }

    public void setRequestingUser(User user) {
        put(REQUESTING_USER, user.getParseUser());
    }

    public User getRequestingUser() {
        ParseUser parseUser = getParseUser(REQUESTING_USER);
        try {
            return new User(parseUser.fetchIfNeeded());
        } catch (ParseException e) {
            return null;
        }
    }

    public AdoptionPet getAdoptionPet() {
        AdoptionPet adoptionPet = (AdoptionPet) getParseObject(ADOPTION_PET);
        try {
            return adoptionPet.fetchIfNeeded();
        } catch (ParseException e) {
            return null;
        }
    }

    public void setAdoptionPet(AdoptionPet adoptionPet) {
        put(ADOPTION_PET, adoptionPet);
    }

    public String getMessage() {
        return getString(MESSAGE);
    }

    public void setMessage(String message) {
        put(MESSAGE, message);
    }

    public String getState() {
        return getString(STATE);
    }

    public void setState(String state) {
        put(STATE, state);
    }

    public String getDate() {
        return getString(DATE);
    }

    public void setDate(String date) {
        put(DATE, date);
    }
}
