package ar.uba.fi.nicodiaz.mascota;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements DatabaseOperationListener {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};

    private ProgressDialog progressDialog;
    private LoginActivity activity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_login);
                ButterKnife.inject(mActivity);
            }
        };
        WaitForInternet.setCallback(callback);
    }

    public void onFacebookLoginClick(View v) {
        onLoadingStart(true);
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this,
                mPermissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (isDestroyed()) {
                            return;
                        }
                        if (user == null) {
                            onLoadingFinish();
                            if (e != null) {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.inicio_sesion_facebook_error), Toast.LENGTH_SHORT).show();
                                Log.w(TAG, getBaseContext().getResources().getString(R.string.inicio_sesion_facebook_error) + e.toString());
                            }
                        } else {
                            FacebookUtils facebookUtils = FacebookUtils.getInstance();
                            Intent intent;
                            UserService userService = UserService.getInstance();
                            try {
                                if (user.isNew() || !userService.hasSavedInformation()) {
                                    facebookUtils.asociarUsuarioConDatosDeFacebook(activity);
                                } else if (!userService.isBanned(user)) {
                                    intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    ParseUser.logOut();
                                    onLoadingFinish();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle(R.string.USUARIO_BLOQUEADO_TITULO);
                                    builder.setMessage(getString(R.string.USUARIO_BLOQUEADO_MENSAJE));
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            } catch (ApplicationConnectionException ex) {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_conectividad) + ex.toString(), Toast.LENGTH_SHORT).show();
                            }

                            onLoadingFinish();
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

    private void onLoadingStart(boolean showSpinner) {
        if (showSpinner) {
            progressDialog = ProgressDialog.show(LoginActivity.this, "",
                    getString(R.string.loading_dialog_text), true, false);
        }
    }

    public void onLoadingFinish() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onOperationSuccess() {
        Intent intent = new Intent(LoginActivity.this, RegistrarDatosPersonalesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
