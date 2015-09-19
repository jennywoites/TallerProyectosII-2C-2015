package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;

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
            return new User(currentUser);
        }

        return null;
    }

    public void saveUser(User user) {
        user.getParseUser().saveInBackground();
    }

    public boolean userLogged() {
        return getUser() != null;
    }

    public Boolean hasSavedInformation() throws ApplicationConnectionException {
        User user = getUser();

        if (user != null) {
            return user.getAddress() != null;
        }

        return false;
    }
}
