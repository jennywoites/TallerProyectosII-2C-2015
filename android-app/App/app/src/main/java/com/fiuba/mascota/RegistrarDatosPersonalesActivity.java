package com.fiuba.mascota;

import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rha.app.R;

public class RegistrarDatosPersonalesActivity extends AppCompatActivity {

    @InjectView(R.id.input_telefono) EditText _telefonoText;
    @InjectView(R.id.input_ciudad) EditText _ciudadText;
    @InjectView(R.id.input_calle) EditText _calleText;
    @InjectView(R.id.input_codigo_postal) EditText _zipCodText;
    @InjectView(R.id.btn_save) Button _saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regitrar_datos_personales);
        ButterKnife.inject(this);
        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarDatos();
            }
        });
    }

    private void confirmarDatos()
    {
        ParseUser user = ParseUser.getCurrentUser();
        String telefono = _telefonoText.getText().toString();
        String ciudad = _ciudadText.getText().toString();
        String calle = _calleText.getText().toString();
        String zipCode = _zipCodText.getText().toString();

        user.put("phone", telefono);
        user.put("city",ciudad);
        user.put("street",calle);
        user.put("zipcode",zipCode);

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
}
