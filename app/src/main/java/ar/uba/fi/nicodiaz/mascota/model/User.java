package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by Juan Manuel Romera on 18/9/2015.
 */
public class User {

    public static String USER_NAME_FIELD = "name";
    public static String USER_GENDER_FIELD = "gender";
    public static String USER_PHONE_FIELD = "phone";
    public static String USER_ADDRESS_FIELD = "address";

    private ParseUser user;

    public User(ParseUser user) {
        this.user = user;
    }


    public void setEmail(String email) {
        this.user.setEmail(email);
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public String getName() {
        return user.getString(USER_NAME_FIELD);
    }

    public void setName(String name) {
        user.put(USER_NAME_FIELD, name);
    }

    public String getGender() {
        return user.getString(USER_GENDER_FIELD);
    }

    public void setGender(String gender) {
        user.put(USER_GENDER_FIELD, gender);
    }

    public String getTelephone() {
        return user.getString(USER_PHONE_FIELD);
    }

    public void setTelephone(String telephone) {
        user.put(USER_PHONE_FIELD, telephone);
    }

    public Address getAddress() {
        return (Address) user.getParseObject(USER_ADDRESS_FIELD);
    }

    public void setAddress(Address address) {
        user.put(USER_ADDRESS_FIELD, address);
    }

    public ParseUser getParseUser() {
        return user;
    }
}
