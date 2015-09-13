package com.fiuba.mascota;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

public class HomeActivity extends ActionBarActivity {

    @InjectView(R.id.btn_salir)
    Button _btnSalir;
    @InjectView(R.id.btn_adoptar)
    Button _btnSdoptar;
    @InjectView(R.id.btn_notificar_mascota_perdida)
    Button _btnNotificarMascotaPerdida;
    @InjectView(R.id.welcome_text)
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        CharSequence welcomeMessage = "Welcome " + ParseUser.getCurrentUser().get("name");
        welcomeText.setText(welcomeMessage);

        _btnSalir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logout();
            }
        });

        _btnSdoptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AdopcionActivity.class));
            }
        });
        _btnNotificarMascotaPerdida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PerdidoActivity.class));
            }
        });
    }

    private void logout() {
        //Call logout
        ParseUser.logOut();

        Intent intent = new Intent(HomeActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
