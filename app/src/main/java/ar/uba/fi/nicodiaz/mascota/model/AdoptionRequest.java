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

    public RequestState getState() {
        String text = getString(STATE);
        return RequestState.getState(text);
    }

    public void setState(RequestState state) {
        put(STATE, state.toString());
    }

    public String getDate() {
        return getString(DATE);
    }

    public void setDate(String date) {
        put(DATE, date);
    }

    public void accept() {
        setState(RequestState.ACCEPTED);
    }

    public void ignore() {
        setState(RequestState.IGNORED);
    }

    public void reject() {
        setState(RequestState.REJECTED);
    }

    public boolean isPending() {
        return getState().equals(RequestState.PENDING);
    }

    public boolean isAccepted() {
        return getState().equals(RequestState.ACCEPTED);
    }

    public boolean isRejected() {
        return getState().equals(RequestState.REJECTED);
    }

    public boolean isIgnored() {
        return getState().equals(RequestState.IGNORED);
    }

    public boolean isConfirmed() {
        return getState().equals(RequestState.CONFIRMED);
    }

    public void confirm() {
        setState(RequestState.CONFIRMED);
    }

    public void pend() {
        setState(RequestState.PENDING);
    }
}
