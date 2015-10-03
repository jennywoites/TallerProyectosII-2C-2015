package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.utils.ParseProxyObject;

/**
 * Created by Juan Manuel Romera on 18/9/2015.
 */
public class User {

    public static String USER_OBJECT_ID = "objectId";
    public static String USERNAME = "username";
    public static String USER_NAME_FIELD = "name";
    public static String USER_EMAIL_FIELD = "email";
    public static String USER_GENDER_FIELD = "gender";
    public static String USER_PHONE_FIELD = "phone";
    public static String USER_ADDRESS_FIELD = "address";
    public static String MALE = "male";
    public static String FEMALE = "female";

    private ParseUser user;

    public User(ParseUser user) {
        this.user = user;
    }

    public User(ParseProxyObject ppo) {
        this.user = new ParseUser();
        setUsername(ppo.getString(USERNAME));
        setEmail(ppo.getString(USER_EMAIL_FIELD));
        setName(ppo.getString(USER_NAME_FIELD));
        setGender(ppo.getString(USER_GENDER_FIELD));
        setTelephone(ppo.getString(USER_PHONE_FIELD));
        ParseProxyObject addressPPO = ppo.getParseObject(USER_ADDRESS_FIELD);
        setAddress(new Address(addressPPO));
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

    public void setUsername(String username) {
        user.setUsername(username);
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
        try {
            return (Address) user.getParseObject(USER_ADDRESS_FIELD).fetchIfNeeded();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setAddress(Address address) {
        user.put(USER_ADDRESS_FIELD, address);
    }

    public ParseUser getParseUser() {
        return user;
    }

    public boolean isMale() {
        String gender = getGender();
        if (gender != null) {
            return gender.equals(MALE);
        } else return false;
    }

    public boolean isFemale() {
        String gender = getGender();
        if (gender != null) {
            return gender.equals(FEMALE);
        } else return false;
    }


}
