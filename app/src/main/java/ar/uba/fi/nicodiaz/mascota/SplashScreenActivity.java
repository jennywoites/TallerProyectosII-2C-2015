package ar.uba.fi.nicodiaz.mascota;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nicolas on 13/09/15.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.splash_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this, DispatchActivity.class);
                startActivity(mainIntent);
                finish(); // TODO: not sure si hará quilombo con Parse y login.
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

        //Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "EcXAT5pXyPP4x874uRSPKgC6cMgd3BBNdK6UKKAq", "cJ8rMk6fCUSx9uTNbz1F915yKaJawtN3VQSTbopX");

        //Facebook Connect
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);
    }
}
