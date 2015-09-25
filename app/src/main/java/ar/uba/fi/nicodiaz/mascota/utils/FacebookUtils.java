package ar.uba.fi.nicodiaz.mascota.utils;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import ar.uba.fi.nicodiaz.mascota.DatabaseOperationListener;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;

/**
 * Created by Juan Manuel Romera on 13/9/2015.
 */
public class FacebookUtils {

    private static FacebookUtils ourInstance = new FacebookUtils();

    public static FacebookUtils getInstance() {
        return ourInstance;
    }

    private FacebookUtils() {
    }

    public void asociarUsuarioConDatosDeFacebook(final DatabaseOperationListener databaseOperationListener) {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {

                            String gender;
                            String email;
                            String name;
                            JSONObject location;

                            try {

                                UserService userService = UserService.getInstance();
                                User user = userService.getUser();

                                if (jsonObject.getString("gender") != null) {
                                    gender = jsonObject.getString("gender");
                                    user.setGender(gender);
                                }

                                if (jsonObject.getString("email") != null) {
                                    email = jsonObject.getString("email");
                                    user.setEmail(email);
                                }

                                if (jsonObject.getString("name") != null) {
                                    name = jsonObject.getString("name");
                                    user.setName(name);
                                }

                                userService.saveUser(user,databaseOperationListener);

                            } catch (JSONException e) {
                            }
                            databaseOperationListener.onOperationSuccess();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email, gender, name");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
