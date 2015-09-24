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
}
