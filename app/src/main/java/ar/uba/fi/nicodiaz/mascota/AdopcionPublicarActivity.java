package ar.uba.fi.nicodiaz.mascota;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
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

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;

public class AdopcionPublicarActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1046;
    private Toolbar toolbar;
    private Bitmap selectedBitmap;
    private ImageView image;
    private Button selectImageButton;
    private ParseFile photoFile;
    private ProgressDialog progressDialog;
    private AdoptionPet pet;


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Perderá los cambios si vuelve atrás.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AdopcionPublicarActivity.this.finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopcion_publicar);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO: Si se encuentra forma de capturar este evento, descomentarlo y mandarlo al dialogo de onBackPressed()
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image = (ImageView) findViewById(R.id.imageView);
        selectImageButton = (Button) findViewById(R.id.button1);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickPhoto();
            }
        });

        pet = new AdoptionPet();
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
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                saveScaledPhoto(selectedBitmap);
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

    private void saveScaledPhoto(Bitmap picture) {
        uploadingPhoto();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        byte[] scaledData = bos.toByteArray();

        // Save the scaled image to Parse
        photoFile = new ParseFile(picture.hashCode() + ".jpeg", scaledData);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                finishUploadingPhoto();
                if (e != null) {
                    Toast.makeText(AdopcionPublicarActivity.this,
                            getString(R.string.error_uploading_photo) + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    pet.setPicture(photoFile);
                    image.setImageBitmap(selectedBitmap);
                }
            }
        });
    }

    private void uploadingPhoto() {
        progressDialog = ProgressDialog.show(this, null,
                getString(R.string.subiendo_foto), true, false);
    }

    private void finishUploadingPhoto() {
        if (progressDialog != null) {
            progressDialog.dismiss();
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
        String pets = this.getPetsValue();
        String children = this.getChildrenValue();
        String socialNotes = ((EditText) findViewById(R.id.txtSocialNotes)).getText().toString();
        String medicine = this.getMedicineValue();
        String medicineTime = this.getMedicineTimeValue();
        String medicineNotes = ((EditText) findViewById(R.id.txtMedicineNotes)).getText().toString();

        User user = UserService.getInstance().getUser();
        pet.setName(name);
        pet.setAgeRange(ageRange);
        pet.setDescription(description);
        pet.setGender(gender);
        pet.setKind(kind);
        pet.setOwner(user);
        pet.setOtherPets(pets);
        pet.setChildren(children);
        pet.setSocialNotes(socialNotes);
        pet.setMedicine(medicine);
        pet.setMedicineTime(medicineTime);
        pet.setMedicineNotes(medicineNotes);

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

    private String getPetsValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgPets);
        String radiovalue = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        return (radiovalue);
    }

    private String getChildrenValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgChildren);
        String radiovalue = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        return (radiovalue);
    }

    private String getMedicineValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgMedicine);
        String radiovalue = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        return (radiovalue);
    }

    private String getMedicineTimeValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgMedicineTime);
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
