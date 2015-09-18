package ar.uba.fi.nicodiaz.mascota;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseObject;

import java.io.IOException;

public class AdopcionPublicarActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1046;
    private Toolbar toolbar;
    private ImageView image;
    private Button selectImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopcion_publicar);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image = (ImageView) findViewById(R.id.imageView);
        selectImageButton = (Button) findViewById(R.id.button1);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickPhoto();
            }
        });
    }

    public void onPickPhoto() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri photoUri = data.getData();
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                image.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
