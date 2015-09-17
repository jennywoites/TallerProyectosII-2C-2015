package ar.uba.fi.nicodiaz.mascota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

/**
 * Created by nicolas on 13/09/15.
 */
public class DispatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
