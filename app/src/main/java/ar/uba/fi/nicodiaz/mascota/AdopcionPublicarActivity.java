package ar.uba.fi.nicodiaz.mascota;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;

import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;

public class AdopcionPublicarActivity extends AppCompatActivity {

    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;

    private static final int PICK_IMAGE = 1046;
    private Toolbar toolbar;
    private Bitmap selectedBitmap;
    private ImageView image;
    private Button selectImageButton;
    private ParseFile photoFile;
    private ProgressDialog progressDialog;
    private AdoptionPet pet;

    private HashSet<Uri> mMedia;
    private Button selectImageButton2;


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

        selectImageButton2 = (Button) findViewById(R.id.button2);
        selectImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImages();
            }
        });

        mMedia = new HashSet<Uri>();

        pet = new AdoptionPet();
    }

    // Galeria anterior:
    public void onPickPhoto() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, PICK_IMAGE);


    }

    private void getImages() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.ColorPrimary)
                .setTabSelectionIndicatorColor(R.color.white)
                .setCameraButtonColor(R.color.ColorPrimary)
                .build();
        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (parcelableUris == null) {
                    return;
                }

                // Java doesn't allow array casting, this is a little hack
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);

                if (uris != null) {
                    for (Uri uri : uris) {
                        Log.i(String.valueOf(Log.INFO), "Selected uri: " + uri);
                        mMedia.add(uri);
                    }

                    //showMedia();
                }
            }
            else if (requestCode == PICK_IMAGE) {
                if (intent != null) {
                    Uri photoUri = intent.getData();
                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        saveScaledPhoto(selectedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
            case R.id.action_cerrar:
                onBackPressed();
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
        String urlOne = ((EditText) findViewById(R.id.txtVideoOne)).getText().toString();
        String urlTwo = ((EditText) findViewById(R.id.txtVideoTwo)).getText().toString();
        String urlThree = ((EditText) findViewById(R.id.txtVideoThree)).getText().toString();
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

        valid &= checkOpciones(R.id.rgSpecie, R.id.lbspecies);
        valid &= checkOpciones(R.id.rgSexo, R.id.lbSexo);
        valid &= checkOpciones(R.id.rgAge, R.id.lbAge);
        valid &= checkOpciones(R.id.rgPets, R.id.lbSocialAnimales);
        valid &= checkOpciones(R.id.rgChildren, R.id.lbSocialNiños);
        valid &= checkOpciones(R.id.rgMedicine, R.id.lbMedicina);
        valid &= checkOpciones(R.id.rgMedicineTime, R.id.lbMedicinaTiempo);

        if (image.getDrawable() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.ERROR);
            builder.setMessage(getString(R.string.ERROR_FOTO_NO_INCLUIDA));
            AlertDialog alert = builder.create();
            alert.show();
            valid = false;
        }


        return (valid);
    }

    private boolean checkOpciones(int radioGrupoId, int labelId) {
        String errorOpciones = getResources().getString(R.string.MASCOTA_ADOPCION_ERROR_OPCIONES_VACIAS);
        RadioGroup rg = (RadioGroup) findViewById(radioGrupoId);
        TextView textView = (TextView) findViewById(labelId);

        if (rg.getCheckedRadioButtonId() == -1) {
            textView.setError(errorOpciones);
            return false;
        } else {
            textView.setError(null);
            return true;
        }
    }
}
