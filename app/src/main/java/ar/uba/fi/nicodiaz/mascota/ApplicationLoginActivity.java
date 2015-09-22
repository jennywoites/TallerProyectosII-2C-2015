package ar.uba.fi.nicodiaz.mascota;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Juan Manuel Romera on 19/9/2015.
 */
public class ApplicationLoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegistrarDatosCuentaActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {

        if (!validate()) {
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(ApplicationLoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // Call the Parse login method
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                progressDialog.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error_conectividad), Toast.LENGTH_SHORT).show();

                } else {
                    // Start an intent for the dispatch activity
                    Intent intent;
                    try {

                        Boolean userHasSavedInformation = UserService.getInstance().hasSavedInformation();

                        if (!userHasSavedInformation) {
                            intent = new Intent(ApplicationLoginActivity.this, RegistrarDatosPersonalesActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        } else {
                            intent = new Intent(ApplicationLoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        }

                    } catch (ApplicationConnectionException ex) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.error_conectividad), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String username = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        String errorEmail = getResources().getString(R.string.error_email_login);
        String errorPassword = getResources().getString(R.string.error_password_login);

        if (username.isEmpty()) {
            _emailText.setError(errorEmail);
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(errorPassword);
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
