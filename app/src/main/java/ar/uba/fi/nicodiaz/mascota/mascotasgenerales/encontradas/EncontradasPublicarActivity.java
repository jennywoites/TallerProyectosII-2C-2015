package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.encontradas;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Address;
import ar.uba.fi.nicodiaz.mascota.model.FoundPet;
import ar.uba.fi.nicodiaz.mascota.model.FoundPetState;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;
import ar.uba.fi.nicodiaz.mascota.utils.ErrorUtils;
import ar.uba.fi.nicodiaz.mascota.utils.PhotoUtils;
import ar.uba.fi.nicodiaz.mascota.utils.PlaceAutoCompleteAdapter;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;
import ar.uba.fi.nicodiaz.mascota.utils.YouTubeUtils;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

public class EncontradasPublicarActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, GoogleApiClient.OnConnectionFailedListener {
    private Toolbar toolbar;
    private Button selectImageButton;
    private FoundPet pet;
    private List<Bitmap> photos;
    private LinearLayout photos_layout;
    private TextView photos_empty;

    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    // DatePicker Fecha encuentro de la Mascota.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    // -*-*-*-*-*-*-*-*-*-*-*
    // Completa la dirección.
    // -*-*-*-*-*-*-*-*-*-*-*
    AutoCompleteTextView _addressAutoCompleteText;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    private Address direccion;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Perderá los cambios si vuelve atrás.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EncontradasPublicarActivity.this.finish();
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
                setContentView(R.layout.activity_encontrada_publicar);

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

                pet = new FoundPet();


                // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
                // DatePicker Fecha última vista de la Mascota.
                // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
                ((EncontradasPublicarActivity) mActivity).updateLabel();

                ((TextView) findViewById(R.id.txtDate)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(EncontradasPublicarActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                // -*-*-*-*-*-*-*-*-*-*-*
                // Completa la dirección.
                // -*-*-*-*-*-*-*-*-*-*-*
                mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                        .enableAutoManage((EncontradasPublicarActivity) mActivity, 0, (EncontradasPublicarActivity) mActivity)
                        .addApi(Places.GEO_DATA_API)
                        .build();


                _addressAutoCompleteText = (AutoCompleteTextView) findViewById(R.id.input_direccion);
                _addressAutoCompleteText.setOnItemClickListener((EncontradasPublicarActivity) mActivity);


                mAdapter = new PlaceAutoCompleteAdapter(mActivity, android.R.layout.simple_list_item_1,
                        mGoogleApiClient, BOUNDS_GREATER_SYDNEY, null);
                _addressAutoCompleteText.setAdapter(mAdapter);


            }
        };
        WaitForInternet.setCallback(callback);
    }

    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    // Actualiza la vista con la fecha seleccionada.
    // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ((TextView) findViewById(R.id.txtDate)).setText(sdf.format(myCalendar.getTime()));
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

    // -*-*-*-*-*-*-*-*-*-*-*
    // Completa la dirección.
    // -*-*-*-*-*-*-*-*-*-*-*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
        final String placeId = String.valueOf(item.placeId);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);

        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    // Request did not complete successfully
                    places.release();
                    direccion = null;
                    return;
                }
                // Get the Place object from the buffer.
                final Place place = places.get(0);
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                String address = place.getAddress().toString();
                double longitude = place.getLatLng().longitude;
                double latitude = place.getLatLng().latitude;


                List<android.location.Address> addresses = null;
                String locality = "";
                String subLocality = "";
                try {
                    addresses = gcd.getFromLocation(latitude, longitude, 1);
                    if (addresses.size() > 0) {
                        locality = addresses.get(0).getLocality();
                        subLocality = addresses.get(0).getSubLocality();
                    }

                } catch (IOException e) {
                    direccion = null;
                    return;
                }

                direccion = new Address(address, latitude, longitude, locality, subLocality);
                places.release();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_encontrada_publicar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_confirmar_agregar_mascota_encontrada:
                if (!this.validate()) {
                    return false;
                }
                if (UserService.getInstance().isBanned(ParseUser.getCurrentUser())) {
                    Toast.makeText(EncontradasPublicarActivity.this, "Ocurrió un problema al publicar. Intente más tarde.", Toast.LENGTH_SHORT).show();
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
        String raza = ((AutoCompleteTextView) findViewById(R.id.txtRace)).getText().toString();
        String kind = this.getSpecieValue();
        String gender = this.getSexoValue();
        String urlOne = YouTubeUtils.parseYouTubeVideoUrl(((EditText) findViewById(R.id.txtVideoOne)).getText().toString());
        String urlTwo = YouTubeUtils.parseYouTubeVideoUrl(((EditText) findViewById(R.id.txtVideoTwo)).getText().toString());
        String urlThree = YouTubeUtils.parseYouTubeVideoUrl(((EditText) findViewById(R.id.txtVideoThree)).getText().toString());
        User user = UserService.getInstance().getUser();
        Date lastKnowDate = this.parseStringToDate(((TextView) findViewById(R.id.txtDate)).getText().toString());
        Address lastKnowAddress = this.direccion;

        pet.setGender(gender);
        pet.setBreed(raza);
        pet.setKind(kind);
        pet.setPublisher(user);
        pet.setVideo1(urlOne);
        pet.setVideo2(urlTwo);
        pet.setVideo3(urlThree);
        pet.setLastKnowDate(lastKnowDate);
        pet.setLastKnowAddress(lastKnowAddress);
        pet.setState(FoundPetState.PUBLISHED);
        pet.setBanned(false);
    }

    private Date parseStringToDate(String lastKnowDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = null;

        try {
            convertedDate = dateFormat.parse(lastKnowDate);
        } catch (java.text.ParseException e) {
        }

        return (convertedDate);
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
        PetServiceFactory.getInstance().saveFoundPet(pet);
        Toast.makeText(EncontradasPublicarActivity.this, "¡Mascota publicada!", Toast.LENGTH_SHORT).show();
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

    private boolean validate() {
        boolean valid = true;

        EditText address = (EditText) findViewById(R.id.input_direccion);
        String addressText = ((EditText) findViewById(R.id.input_direccion)).getText().toString();
        String errorAddress = getResources().getString(R.string.MASCOTA_ADOPCION_ERROR_EMPTY_ADDRESS);

        if (direccion == null) {
            address.setError(errorAddress);
            valid = false;
        } else {
            address.setError(null);
        }

        valid &= checkOpciones(R.id.rgSpecie, R.id.lbspecies);
        valid &= checkOpciones(R.id.rgSexo, R.id.lbSexo);

        // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
        // La última fecha conocida no puede ser futura.
        // -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
        if (valid) {
            Date lastKnowDate = parseStringToDate(((TextView) findViewById(R.id.txtDate)).getText().toString());
            Date dateNow = Calendar.getInstance().getTime();

            if (lastKnowDate.compareTo(dateNow) > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.ADVERTENCIA);
                builder.setMessage(getString(R.string.ERROR_FECHA_FUTURA));
                AlertDialog alert = builder.create();
                alert.show();
                valid = false;
            }
        }

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