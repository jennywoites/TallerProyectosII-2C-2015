package ar.uba.fi.nicodiaz.mascota.MascotasGenerales;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ar.uba.fi.nicodiaz.mascota.R;

public class SolicitarAdopcionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_adopcion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // TODO: setear todos los campos para la mascota actual

        Button adoptBtn = (Button) findViewById(R.id.adopt);
        adoptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar en base de datos request de adopcion para la mascota actual.
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAtras();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solicitar_adopcion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_close:
                volverAtras();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        volverAtras();
    }

    private void volverAtras() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.slide_in_2, R.anim.slide_out_2);
    }
}
