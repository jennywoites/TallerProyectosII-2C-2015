package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;
import ar.uba.fi.nicodiaz.mascota.utils.UserMapping;

/**
 * Created by Juan Manuel Romera on 18/9/2015.
 */
public class UserService {
    private static UserService ourInstance = new UserService();

    public static UserService getInstance() {
        return ourInstance;
    }

    private UserService() {
    }

    public User getUser() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            User user = UserMapping.mapUser(currentUser);
            return user;
        }
        return null;
    }

    public void saveUser(User user) {
        ParseUser currentUser = UserMapping.mapUser(user);
        currentUser.saveInBackground();
    }

    public boolean userLogged() {
        return ParseUser.getCurrentUser() != null;
    }

}
