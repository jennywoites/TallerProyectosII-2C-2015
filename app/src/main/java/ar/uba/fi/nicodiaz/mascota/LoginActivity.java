package ar.uba.fi.nicodiaz.mascota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;
import ar.uba.fi.nicodiaz.mascota.utils.FacebookUtils;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

    }

    public void onFacebookLoginClick(View v) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, mPermissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user != null) {
                    FacebookUtils facebookUtils = FacebookUtils.getInstance();
                    Intent intent;
                    UserService userService = UserService.getInstance();
                    try {
                        if (user.isNew() || !userService.hasSavedInformation()) {
                            facebookUtils.asociarUsuarioConDatosDeFacebook();
                            intent = new Intent(LoginActivity.this, RegistrarDatosPersonalesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        } else {
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        }

                    } catch (ApplicationConnectionException e) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.error_conectividad), Toast.LENGTH_SHORT);
                        return;
                    }


                    startActivity(intent);

                }

            }
        });
    }

    public void onApplicationLoginClick(View v) {
        Intent intent = new Intent(LoginActivity.this, ApplicationLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
}
