package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.adopcion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPetState;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;
import ar.uba.fi.nicodiaz.mascota.utils.ErrorUtils;
import ar.uba.fi.nicodiaz.mascota.utils.PhotoUtils;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;
import ar.uba.fi.nicodiaz.mascota.utils.YouTubeUtils;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

public class AdopcionPublicarActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button selectImageButton;
    private AdoptionPet pet;
    private List<Bitmap> photos;
    private LinearLayout photos_layout;
    private TextView photos_empty;
    private EditText nameEditText;
    private CheckBox transitoCheckBox;
    private boolean enTransito;

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

        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_adopcion_publicar);

                // Toolbar
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                //TODO: Si se encuentra forma de capturar este evento, descomentarlo y mandarlo al dialogo de onBackPressed()
                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                final AutoCompleteTextView raza = (AutoCompleteTextView) findViewById(R.id.txtRace);
                String[] razaPerros = getResources().getStringArray(R.array.dogs);
                String[] razaGatos = getResources().getStringArray(R.array.cats);

                final ArrayAdapter<String> adapterDogs = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, razaPerros);
                final ArrayAdapter<String> adapterCats = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, razaGatos);


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

                RadioGroup medicineYesNo = (RadioGroup) findViewById(R.id.rgMedicine);

                medicineYesNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rdSi:
                                findViewById(R.id.medicineTimeLayout).setVisibility(View.VISIBLE);
                                ((RadioButton) findViewById(R.id.rdNoToma)).setChecked(true);
                                break;
                            case R.id.rdNo:
                                findViewById(R.id.medicineTimeLayout).setVisibility(View.GONE);
                                ((RadioButton) findViewById(R.id.rdNoToma)).setChecked(true);
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

                // Solo permite caracteres de tipo letra o espacio
                nameEditText = (EditText) findViewById(R.id.txtName);
                nameEditText.setFilters(new InputFilter[]{
                        new InputFilter() {
                            @Override
                            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                                if (source.equals("")) { // for backspace
                                    return source;
                                }
                                if (source.toString().matches("[a-zA-Z ]+")) {
                                    return source;
                                }
                                return "";
                            }
                        }
                });

                enTransito = false;
                transitoCheckBox = (CheckBox) findViewById(R.id.transito);
                transitoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        enTransito = isChecked;
                    }
                });

                pet = new AdoptionPet();
            }
        };
        WaitForInternet.setCallback(callback);
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

        new Picker.Builder(this, new Picker.PickListener() {
            @Override
            public void onPickedSuccessfully(ArrayList<ImageEntry> images) {
                for (ImageEntry image : images) {
                    Log.d(String.valueOf(Log.DEBUG), image.path);
                    addPhoto(new File(image.path));
                }
                showMedia();
            }

            @Override
            public void onCancel() {
            }
        }, R.style.GalleryPicker)
                .setFabBackgroundColor(getResources().getColor(R.color.ColorPrimary))
                .setDoneFabIconTintColor(getResources().getColor(R.color.ColorPrimaryLight))
                .setFabBackgroundColorWhenPressed(getResources().getColor(R.color.ColorPrimaryDark))
                .setCaptureItemIconTintColor(getResources().getColor(R.color.ColorPrimaryLight))
                .setAlbumNameTextColor(getResources().getColor(R.color.ColorPrimaryLight))
                .setBackBtnInMainActivity(true)
                .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                .setLimit(5)
                .build()
                .startActivity();
    }

    private Bitmap addPhoto(File file) {
        Bitmap image;
        try {
            image = PhotoUtils.resizeImage(file, PhotoUtils.PHOTO_WIDHT_MAX, PhotoUtils.PHOTO_HEIGHT_MAX);
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
                if (!this.validate()) {
                    return false;
                }
                if (UserService.getInstance().isBanned(ParseUser.getCurrentUser())) {
                    Toast.makeText(AdopcionPublicarActivity.this, "Ocurrió un problema al publicar. Intente más tarde.", Toast.LENGTH_SHORT).show();
                    finish();
                    return false;
                }
                new SavePetTask(this).execute();
                return true;
            case R.id.action_cerrar:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SavePetTask extends AsyncTask<Void, Integer, Boolean> {

        private ProgressDialog progressDialog;
        private Context context;

        public SavePetTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, null,
                    getString(R.string.grabando_mascota), true, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            createPet();

            //Uploading Photo
            int indexPhoto = 1;
            for (Bitmap photo : photos) {
                publishProgress(indexPhoto);
                try {
                    uploadPhoto(photo);
                } catch (ApplicationConnectionException e) {
                    return false;
                }
                indexPhoto++;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                progressDialog.dismiss();
                finishAndSavePet();
            } else {
                progressDialog.dismiss();
                AlertDialog noConnectionDialog = ErrorUtils.createNoConnectionDialog(context.getResources().getString(R.string.ERROR_ALTA_MASCOTA), context);
                noConnectionDialog.show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Integer indexPhoto = values[0];
            String message = "Subiendo Foto " + indexPhoto + " de " + photos.size();
            progressDialog.setMessage(message);
        }
    }


    private void createPet() {
        String name = nameEditText.getText().toString();
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
        String urlOne = YouTubeUtils.parseYouTubeVideoUrl(((EditText) findViewById(R.id.txtVideoOne)).getText().toString());
        String urlTwo = YouTubeUtils.parseYouTubeVideoUrl(((EditText) findViewById(R.id.txtVideoTwo)).getText().toString());
        String urlThree = YouTubeUtils.parseYouTubeVideoUrl(((EditText) findViewById(R.id.txtVideoThree)).getText().toString());
        User user = UserService.getInstance().getUser();

        pet.setName(name);
        pet.setAgeRange(ageRange);
        pet.setDescription(description);
        pet.setGender(gender);
        pet.setBreed(raza);
        pet.setKind(kind);
        pet.setOwner(user);
        pet.setLocation(user.getAddress());
        pet.setOtherPets(pets);
        pet.setChildren(children);
        pet.setSocialNotes(socialNotes);
        pet.setMedicine(medicine);
        pet.setMedicineTime(medicineTime);
        pet.setMedicineNotes(medicineNotes);
        pet.setVideo1(urlOne);
        pet.setVideo2(urlTwo);
        pet.setVideo3(urlThree);
        pet.setState(AdoptionPetState.PUBLISHED);
        pet.setTransito(enTransito);
        pet.setBanned(false);
    }

    private void uploadPhoto(Bitmap photo) throws ApplicationConnectionException {

        Bitmap bitmapResized = PhotoUtils.resizeImage(photo, PhotoUtils.PHOTO_WIDHT_MAX, PhotoUtils.PHOTO_HEIGHT_MAX);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapResized.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        byte[] scaledData = bos.toByteArray();
        // Save the scaled image to Parse
        final ParseFile photoFile = new ParseFile(photo.hashCode() + ".jpeg", scaledData);

        try {
            photoFile.save();
        } catch (ParseException e) {
            throw new ApplicationConnectionException();
        }
        pet.setPicture(photoFile);
    }

    private void finishAndSavePet() {
        PetServiceFactory.getInstance().saveAdoptionPet(pet);
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

        // Checkeo de medicina:
        if (((RadioButton) findViewById(R.id.rdSi)).isChecked() && ((EditText) findViewById(R.id.txtMedicineNotes)).getText().toString().isEmpty()) {
            EditText notes = (EditText) findViewById(R.id.txtMedicineNotes);
            notes.setError("Debes especificar la medicina que toma");
            valid = false;
        }
        if (((RadioButton) findViewById(R.id.rdSi)).isChecked() && ((RadioButton) findViewById(R.id.rdNoToma)).isChecked()) {
            TextView medicineTime = (TextView) findViewById(R.id.lbMedicinaTiempo);
            medicineTime.setError(getResources().getString(R.string.MASCOTA_ADOPCION_ERROR_OPCIONES_VACIAS));
            valid = false;
        }
        // Fin checkeo de medicina

        if (valid) {
            if (photos.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.ADVERTENCIA);
                builder.setMessage(getString(R.string.ERROR_FOTO_NO_INCLUIDA));
                AlertDialog alert = builder.create();
                alert.show();
                valid = false;
            }
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