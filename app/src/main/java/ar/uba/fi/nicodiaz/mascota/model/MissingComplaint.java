package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.Complaint;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.User;

/**
 * Created by Juan Manuel Romera on 11/11/2015.
 */
@ParseClassName("DenunciaPublicacionPerdida")
public class MissingComplaint extends ParseObject implements Complaint {

    public static final String INFORMED = "denunciado";
    public static final String INFORMER = "denunciante";
    public static final String INFO = "masInfo";
    public static final String PUBLICATION = "publicacion";
    public static final String KIND = "tipo";

    public MissingComplaint() {
    }

    @Override
    public void setInformed(User user) {
        put(INFORMED, user.getParseUser());
    }

    @Override
    public User getInformed() {
        ParseUser parseUser = getParseUser(INFORMED);
        try {
            return new User(parseUser.fetchIfNeeded());
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void setInformer(User user) {
        put(INFORMER, user.getParseUser());
    }

    @Override
    public User getInformer() {
        ParseUser parseUser = getParseUser(INFORMER);
        try {
            return new User(parseUser.fetchIfNeeded());
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void setInfo(String info) {
        put(INFO, info);
    }

    @Override
    public String getInfo() {
        return getString(INFO);
    }

    @Override
    public void setPublication(Pet pet) {
        put(PUBLICATION, pet);
    }

    @Override
    public Pet getPublication() {
        MissingPet pet = (MissingPet) getParseObject(PUBLICATION);
        try {
            return pet.fetchIfNeeded();
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void setKind(String kind) {
        put(KIND, kind);
    }

    @Override
    public String getKind() {
        return getString(KIND);
    }

}

