package ar.uba.fi.nicodiaz.mascota;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseObject;

import java.io.IOException;

import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.utils.PhotoUtils;

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
        if (!this.validate()) {
            return;
        }

        String name = ((EditText) findViewById(R.id.txtName)).getText().toString();
        String description = ((EditText) findViewById(R.id.txtDescription)).getText().toString();
        String kind = this.getSpecieValue();
        String gender = this.getSexoValue();
        String ageRange = this.getAgeValue();

        User user = UserService.getInstance().getUser();

        AdoptionPet pet = new AdoptionPet();
        pet.setName(name);
        pet.setAgeRange(ageRange);
        pet.setDescription(description);
        pet.setGender(gender);
        pet.setKind(kind);
        pet.setOwner(user);

        PetService.getInstance().saveAdoptionPet(pet);

        Toast.makeText(AdopcionPublicarActivity.this, "¡Mascota publicada!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getSpecieValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgSpecie);
        String radiovalue = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        return (radiovalue);
    }

    private String getSexoValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgSexo);
        String radiovalue = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        return (radiovalue);
    }

    private String getAgeValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgAge);
        String radiovalue = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        return (radiovalue);
    }

    private boolean validate() {
        boolean valid = true;

        EditText name = (EditText) findViewById(R.id.txtName);
        EditText description = (EditText) findViewById(R.id.txtDescription);
        String nameText = ((EditText) findViewById(R.id.txtName)).getText().toString();
        String descriptionText = ((EditText) findViewById(R.id.txtDescription)).getText().toString();
        String errorName = getResources().getString(R.string.MASCOTA_ADOPCION_ERROR_EMPTY_NAME);
        String errorDescription = getResources().getString(R.string.MASCOTA_ADOPCION_ERROR_EMPTY_DESCRIPTION);

        if (nameText.isEmpty()) {
            name.setError(errorName);
            valid = false;
        } else {
            name.setError(null);
        }

        if (descriptionText.isEmpty()) {
            description.setError(errorDescription);
            valid = false;
        } else {
            description.setError(null);
        }

        return (valid);
    }
}
