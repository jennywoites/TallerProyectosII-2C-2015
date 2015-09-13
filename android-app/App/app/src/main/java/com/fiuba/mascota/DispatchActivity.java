package com.fiuba.mascota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

/**
 * Created by JFERRIO on 09/09/2015.
 */
public class DispatchActivity extends AppCompatActivity {

    public DispatchActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {

            String phone = (String) ParseUser.getCurrentUser().get("phone");
            String city = (String) ParseUser.getCurrentUser().get("city");

            if (phone != null || city != null) {
                // Start an intent for the logged in activity
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                // Start an intent for the logged in activity
                startActivity(new Intent(this, RegistrarDatosPersonalesActivity.class));
            }

        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}