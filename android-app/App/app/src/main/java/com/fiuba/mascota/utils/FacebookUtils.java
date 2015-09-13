package com.fiuba.mascota.utils;

import android.location.Location;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void asociarUsuarioConDatosDeFacebook() {
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

                                // Save the user profile info in a user property
                                ParseUser currentUser = ParseUser.getCurrentUser();

                                if (jsonObject.getString("gender") != null)
                                {
                                    gender = jsonObject.getString("gender");
                                    currentUser.put("gender", gender);
                                }

                                if (jsonObject.getString("email") != null)
                                {
                                    email = jsonObject.getString("email");
                                    currentUser.setEmail(email);
                                }

                                if (jsonObject.getString("name") != null)
                                {
                                    name = jsonObject.getString("name");
                                    currentUser.put("name", name);
                                }

                                currentUser.saveInBackground();


                            } catch (JSONException e) {
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,gender,name");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
