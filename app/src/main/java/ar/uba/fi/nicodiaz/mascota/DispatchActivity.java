package ar.uba.fi.nicodiaz.mascota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.model.AddressService;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;

/**
 * Created by nicolas on 13/09/15.
 */
public class DispatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserService userService = UserService.getInstance();
        AddressService addressService = AddressService.getInstance();
        if (userService.userLogged()) {
            String userID = userService.getUser().getID();
            try {
                if (addressService.hasSavedInformation(userID)) {
                    startActivity(new Intent(this, HomeActivity.class));
                } else {
                    ParseUser.logOut();
                    startActivity(new Intent(this, LoginActivity.class));
                }
            } catch (ApplicationConnectionException e) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.error_conectividad), Toast.LENGTH_SHORT);
                startActivity(new Intent(this, LoginActivity.class));
            }

        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
