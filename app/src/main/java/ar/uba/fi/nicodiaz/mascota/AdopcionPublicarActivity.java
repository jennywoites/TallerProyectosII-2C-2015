package ar.uba.fi.nicodiaz.mascota;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;

public class AdopcionPublicarActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1046;
    private static final int PICK_IMAGE_N = 1047;
    private Toolbar toolbar;
    private Bitmap selectedBitmap;
    private Button selectImageButton;
    private AdoptionPet pet;
    private List<Bitmap> photos;
    private LinearLayout photos_layout;
    private TextView photos_empty;

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


        final AutoCompleteTextView raza = (AutoCompleteTextView) findViewById(R.id.txtRace);
        String[] razaPerros = getResources().getStringArray(R.array.dogs);
        //String[] razaGatos = getResources().getStringArray(R.array.cats);
        String[] razaGatos = {};

        final ArrayAdapter<String> adapterDogs = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, razaPerros);
        final ArrayAdapter<String> adapterCats = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, razaGatos);


        RadioGroup species = (RadioGroup) findViewById(R.id.rgSpecie);
        species.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdDog:
                        raza.setAdapter(adapterDogs);
                        break;
                    case R.id.rdCat:
                        raza.setAdapter(adapterCats);
                        break;
                }
            }
        });


        photos = new ArrayList<>();
        photos_layout = (LinearLayout) findViewById(R.id.photos_layout);
        photos_empty = (TextView) findViewById(R.id.selected_photos_empty);
        selectImageButton = (Button) findViewById(R.id.button1);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickPhoto();
            }
        });

        pet = new AdoptionPet();
    }

    private void showMedia() {
        if (photos.isEmpty()) {
            photos_layout.setVisibility(View.GONE);
            photos_empty.setVisibility(View.VISIBLE);
        } else {
            photos_layout.setVisibility(View.VISIBLE);
            photos_empty.setVisibility(View.GONE);
        }

        for (Bitmap photo : photos) {
            ImageView imageView = new ImageView(this);
            imageView.setAdjustViewBounds(true);
            imageView.setMaxHeight(360);
            imageView.setMaxWidth(360);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageBitmap(photo);
            imageView.setVisibility(View.VISIBLE);
            photos_layout.addView(imageView, 0);
        }

    }

    public void onPickPhoto() {
        photos.clear();
        photos_layout.removeAllViewsInLayout();
        photos_layout.setVisibility(View.GONE);
        photos_empty.setVisibility(View.VISIBLE);
        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(pickIntent, PICK_IMAGE_N);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_N) {
                if (intent != null) {
                    Uri uri;
                    if (intent.getClipData() != null) {
                        ClipData clipData = intent.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            uri = item.getUri();
                            Bitmap bitmap = addPhoto(uri);
                            saveScaledPhoto(bitmap);
                        }
                    } else if (intent.getData() != null) {
                        uri = intent.getData();
                        Bitmap bitmap = addPhoto(uri);
                        saveScaledPhoto(bitmap);
                    }
                    showMedia();
                }
            }
        }
    }

    private Bitmap addPhoto(Uri uri) {
        Bitmap image;
        try {
            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (image != null) {
            photos.add(image);
            return image;
        }

        return null;
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

        final ProgressDialog progressDialog = ProgressDialog.show(this, null,
                getString(R.string.subiendo_foto), true, false);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 50, bos);

        byte[] scaledData = bos.toByteArray();

        // Save the scaled image to Parse
        final ParseFile photoFile = new ParseFile(picture.hashCode() + ".jpeg", scaledData);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                progressDialog.dismiss();
                if (e != null) {
                    Toast.makeText(AdopcionPublicarActivity.this,
                            getString(R.string.error_uploading_photo) + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    pet.setPicture(photoFile);
                }
            }
        });
    }

    private void confirmarAgregarMascota() {
        if (!this.validate()) {
            return;
        }

        String name = ((EditText) findViewById(R.id.txtName)).getText().toString();
        String description = ((EditText) findViewById(R.id.txtDescription)).getText().toString();
        String raza = ((AutoCompleteTextView) findViewById(R.id.txtRace)).getText().toString();
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
        pet.setBreed(raza);
        pet.setKind(kind);
        pet.setOwner(user);
        pet.setOtherPets(pets);
        pet.setChildren(children);
        pet.setSocialNotes(socialNotes);
        pet.setMedicine(medicine);
        pet.setMedicineTime(medicineTime);
        pet.setMedicineNotes(medicineNotes);
        pet.setVideo1(urlOne);
        pet.setVideo2(urlTwo);
        pet.setVideo3(urlThree);

        PetService.getInstance().saveAdoptionPet(pet);
        Toast.makeText(AdopcionPublicarActivity.this, "¡Mascota publicada!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getSpecieValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgSpecie);
        return (((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
    }

    private String getSexoValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgSexo);
        return (((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
    }

    private String getAgeValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgAge);
        return (((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
    }

    private String getPetsValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgPets);
        return (((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
    }

    private String getChildrenValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgChildren);
        return (((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
    }

    private String getMedicineValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgMedicine);
        return (((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
    }

    private String getMedicineTimeValue() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgMedicineTime);
        return (((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
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

        if (photos.isEmpty()) {
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
