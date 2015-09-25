package ar.uba.fi.nicodiaz.mascota.model;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import ar.uba.fi.nicodiaz.mascota.DatabaseOperationListener;
import ar.uba.fi.nicodiaz.mascota.MyApplication;
import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;

/**
 * Created by Juan Manuel Romera on 18/9/2015.
 */
public class UserService {
    private static final String TAG = "UserService";
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

    public void saveUser(User user, final DatabaseOperationListener databaseOperationListener) {
        user.getParseUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.w(TAG, MyApplication.getContext().getResources().getString(R.string.error_saving_user) + e.toString());
                }
                databaseOperationListener.onOperationSuccess();
            }
        });
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
