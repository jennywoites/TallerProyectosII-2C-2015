package com.fiuba.mascota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.fiuba.mascota.repository.LocationRepository;
import com.parse.ParseUser;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rha.app.R;

public class RegistrarDatosPersonalesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.input_telefono)
    EditText _telefonoText;
    @InjectView(R.id.input_calle)
    EditText _calleText;
    @InjectView(R.id.input_codigo_postal)
    EditText _zipCodText;
    @InjectView(R.id.btn_save)
    Button _saveButton;
    @InjectView(R.id.spinner_state)
    Spinner spinnerState;
    @InjectView(R.id.spinner_city)
    Spinner spinnerCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regitrar_datos_personales);
        ButterKnife.inject(this);
        loadSpinners();
        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarDatos();
            }
        });

        spinnerState.setOnItemSelectedListener(this);
    }

    private void loadSpinners() {
        LocationRepository locationRepository = LocationRepository.getInstance();
        List<String> states = locationRepository.getStatesByCountry("Argentina");
        ArrayAdapter<String> stateAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, states);

        stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerState.setAdapter(stateAdapter);
        String state = spinnerState.getSelectedItem().toString();
        loadCitySpinner(state);
    }

    private void loadCitySpinner(String state) {
        List<String> cities = LocationRepository.getInstance().getCityByState(state);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);
    }

    private void confirmarDatos() {

        if (!validate()) {
            _saveButton.setEnabled(true);
            return;
        }

        _saveButton.setEnabled(false);

        ParseUser user = ParseUser.getCurrentUser();
        String telefono = _telefonoText.getText().toString();
        String provincia = spinnerState.getSelectedItem().toString();
        String ciudad = spinnerCity.getSelectedItem().toString();
        String calle = _calleText.getText().toString();
        String zipCode = _zipCodText.getText().toString();

        user.put("phone", telefono);
        user.put("state", provincia);
        user.put("city", ciudad);
        user.put("street", calle);
        user.put("zipcode", zipCode);

        user.saveInBackground();
        Intent intent = new Intent(RegistrarDatosPersonalesActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registrar_datos_personales, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean validate() {
        boolean valid = true;

        String telefono = _telefonoText.getText().toString();
        String calle = _calleText.getText().toString();
        String zipCode = _zipCodText.getText().toString();

        String errorTelefono = getResources().getString(R.string.error_telefono_sigunp);
        String errorDireccion = getResources().getString(R.string.error_direccion_sigunp);
        String errorZipCode = getResources().getString(R.string.error_zipcode_sigunp);

        if (telefono.isEmpty() || telefono.length() < 8 || telefono.length() > 15) {
            _telefonoText.setError(errorTelefono);
            valid = false;
        } else {
            _telefonoText.setError(null);
        }

        if (calle.isEmpty()) {
            _calleText.setError(errorDireccion);
            valid = false;
        } else {
            _calleText.setError(null);
        }

        if (zipCode.isEmpty() || zipCode.length() != 4) {
            _zipCodText.setError(errorZipCode);
            valid = false;
        } else {
            _zipCodText.setError(null);
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Object itemAtPosition = adapterView.getItemAtPosition(i);
        String state = (String) itemAtPosition;
        loadCitySpinner(state);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
