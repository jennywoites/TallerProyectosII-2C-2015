package ar.uba.fi.nicodiaz.mascota;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ar.uba.fi.nicodiaz.mascota.model.Address;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.utils.PlaceAutoCompleteAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegistrarDatosPersonalesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, GoogleApiClient.OnConnectionFailedListener {

    @InjectView(R.id.input_telefono)
    EditText _telefonoText;
    @InjectView(R.id.input_direccion)
    AutoCompleteTextView _addressAutoCompleteText;
    @InjectView(R.id.input_piso)
    EditText _pisoText;
    @InjectView(R.id.input_depto)
    EditText _deptoText;
    @InjectView(R.id.btn_save)
    Button _saveButton;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    private Address direccion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_regitrar_datos_personales);
                ButterKnife.inject(mActivity);

                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_datos_personales));

                mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                        .enableAutoManage((AppCompatActivity) mActivity, 0, (GoogleApiClient.OnConnectionFailedListener) mActivity)
                        .addApi(Places.GEO_DATA_API)
                        .build();

                // Register a listener that receives callbacks when a suggestion has been selected
                _addressAutoCompleteText.setOnItemClickListener((AdapterView.OnItemClickListener) mActivity);

                // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
                // the entire world.
                mAdapter = new PlaceAutoCompleteAdapter(mActivity, android.R.layout.simple_list_item_1,
                        mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
                _addressAutoCompleteText.setAdapter(mAdapter);

                _saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmarDatos();
                    }
                });
            }
        };
        WaitForInternet.setCallback(callback);
    }

    private void confirmarDatos() {

        if (!validate()) {
            _saveButton.setEnabled(true);
            return;
        }

        _saveButton.setEnabled(false);

        UserService userService = UserService.getInstance();
        User user = userService.getUser();

        String telefono = _telefonoText.getText().toString();
        String piso = _pisoText.getText().toString();
        String depto = _deptoText.getText().toString();
        direccion.setDepartamento(depto);
        direccion.setPiso(piso);
        user.setTelephone(telefono);
        user.setAddress(direccion);
        userService.saveUser(user);

        Intent intent = new Intent(RegistrarDatosPersonalesActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean validate() {
        boolean valid = true;

        String telefono = _telefonoText.getText().toString();
        String piso = _pisoText.getText().toString();
        String depto = _deptoText.getText().toString();

        String errorTelefono = getResources().getString(R.string.error_telefono_sigunp);
        String errorDireccion = getResources().getString(R.string.error_direccion_sigunp);
        String errorPiso = getResources().getString(R.string.error_piso_sigunp);
        String errorDepto = getResources().getString(R.string.error_depto_sigunp);

        if (telefono.isEmpty() || telefono.length() < 8 || telefono.length() > 15) {
            _telefonoText.setError(errorTelefono);
            valid = false;
        } else {
            _telefonoText.setError(null);
        }

        if (direccion == null) {
            _addressAutoCompleteText.setError(errorDireccion);
            valid = false;
        } else {
            _addressAutoCompleteText.setError(null);
        }

        if (piso.length() > 3) {
            _pisoText.setError(errorPiso);
            valid = false;
        } else {
            _pisoText.setError(null);
        }


        if (depto.length() > 2) {
            _deptoText.setError(errorDepto);
            valid = false;
        } else {
            _deptoText.setError(null);
        }


        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Call logout
        ParseUser.logOut();
        Intent intent = new Intent(RegistrarDatosPersonalesActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
        final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
        final String placeId = String.valueOf(item.placeId);
            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);

        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    // Request did not complete successfully
                    places.release();
                    direccion = null;
                    return;
                }
                // Get the Place object from the buffer.
                final Place place = places.get(0);
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                String address = place.getAddress().toString();
                double longitude = place.getLatLng().longitude;
                double latitude = place.getLatLng().latitude;


                List<android.location.Address> addresses = null;
                String locality = "";
                String subLocality = "";
                try {
                    addresses = gcd.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0) {
                        if (addresses.get(0).getLocality() != null)
                            locality = addresses.get(0).getLocality();
                        if (addresses.get(0).getSubLocality() != null)
                            subLocality = addresses.get(0).getSubLocality();
                    }

                } catch (IOException e) {
                    direccion = null;
                    return;
                }

                direccion = new Address(address, latitude, longitude, locality, subLocality);
                places.release();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }
}
