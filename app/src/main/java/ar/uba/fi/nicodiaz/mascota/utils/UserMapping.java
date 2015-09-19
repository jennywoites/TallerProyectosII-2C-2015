package ar.uba.fi.nicodiaz.mascota.utils;

import com.parse.Parse;
import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.model.User;

/**
 * Created by Juan Manuel Romera on 18/9/2015.
 */
public class UserMapping {

    public static String USER_NAME_FIELD = "name";
    public static String USER_GENDER_FIELD = "gender";
    public static String USER_PHONE_FIELD = "phone";

    public static User mapUser(ParseUser parseUser) {
        String email = parseUser.getEmail();
        String name = (String) parseUser.get(USER_NAME_FIELD);
        String gender = (String) parseUser.get(USER_GENDER_FIELD);
        String telephone = (String) parseUser.get(USER_PHONE_FIELD);
        User user = new User();
        user.setID(parseUser.getObjectId());
        user.setEmail(email);
        user.setName(name);
        user.setGender(gender);
        user.setTelephone(telephone);
        return user;
    }

    public static ParseUser mapUser(User user) {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (user.getEmail() != null) {
            currentUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            currentUser.put(USER_NAME_FIELD, user.getName());
        }
        if (user.getGender() != null) {
            currentUser.put(USER_GENDER_FIELD, user.getGender());
        }
        if (user.getTelephone() != null) {
            currentUser.put(USER_PHONE_FIELD, user.getTelephone());
        }
        return currentUser;
    }
}
