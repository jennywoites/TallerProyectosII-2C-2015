package ar.uba.fi.nicodiaz.mascota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;

/**
 * Created by nicolas on 13/09/15.
 */
public class DispatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                UserService userService = UserService.getInstance();
                if (userService.userLogged()) {
                    try {
                        if (userService.hasSavedInformation()) {
                            User user = userService.getUser();
                            if (UserService.getInstance().isBanned(user.getParseUser())) {
                                ParseUser.logOut();
                                Intent i = new Intent(mActivity, LoginActivity.class);
                                i.putExtra("banned", true);
                                startActivity(i);
                            } else {
                                startActivity(new Intent(mActivity, HomeActivity.class));
                            }
                        } else {
                            ParseUser.logOut();
                            startActivity(new Intent(mActivity, LoginActivity.class));
                        }
                    } catch (ApplicationConnectionException e) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.error_conectividad), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(mActivity, LoginActivity.class));
                    }

                } else {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }
            }
        };
        WaitForInternet.setCallback(callback);
    }
}
