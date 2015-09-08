package rha.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseObject;

public class LoginActivity extends ActionBarActivity {
    private EditText TxtNombreView;
    private EditText TxtEmailView;
    private EditText TxtPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TxtNombreView = (EditText) findViewById(R.id.txtNombre);
        TxtEmailView = (EditText) findViewById(R.id.txtEmail);
        TxtPasswordView = (EditText) findViewById(R.id.txtPassword);

        findViewById(R.id.btnVolver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void Crear_Click(View view) {
        String nombre = TxtNombreView.getText().toString();
        String email = TxtEmailView.getText().toString();
        String password = TxtPasswordView.getText().toString();

        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(nombre)) {
            TxtNombreView.setError("Debe ingresar un nombre de Usuario.");
            focusView = TxtNombreView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            TxtPasswordView.setError("Debe ingresar un email.");
            focusView = TxtEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            TxtPasswordView.setError("Debe ingresar un Password.");
            focusView = TxtPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            ParseObject grupo = new ParseObject("Usuario");
            grupo.put("Nombre", nombre);
            grupo.put("Email", email);
            grupo.put("Password", password);
            grupo.saveInBackground();
        }
    }
}
