package ar.uba.fi.nicodiaz.mascota;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseObject;

import butterknife.InjectView;

public class AdopcionPublicarActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopcion_publicar);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adopcion_publicar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_confirmar_agregar_mascota_adopcion:
                confirmarAgregarMascota();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void confirmarAgregarMascota() {
        String name = ((EditText)findViewById(R.id.txtName)).getText().toString();
        String description = ((EditText)findViewById(R.id.txtDescription)).getText().toString();
        String specie = this.getSpecieValue();
        String age = this.getAgeValue();

        // TODO: agrgar el usuario loggeado así se tiene un control de sus mascotas.
        ParseObject mascota = new ParseObject("MascotaAdopcion");
        mascota.put(getResources().getString(R.string.Tabla_MascotaAdopcion_COL_Name), name);
        mascota.put(getResources().getString(R.string.Tabla_MascotaAdopcion_COL_Description), description);
        mascota.put(getResources().getString(R.string.Tabla_MascotaAdopcion_COL_Species), specie);
        mascota.put(getResources().getString(R.string.Tabla_MascotaAdopcion_COL_Age), age);
        mascota.saveInBackground();

        Toast.makeText(AdopcionPublicarActivity.this, "¡Mascota publicada!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getSpecieValue() {
        RadioGroup rg = (RadioGroup)findViewById(R.id.rgSpecie);
        String radiovalue = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        return(radiovalue);
    }

    private String getAgeValue() {
        RadioGroup rg = (RadioGroup)findViewById(R.id.rgAge);
        String radiovalue = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        return(radiovalue);
    }
}
