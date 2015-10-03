package ar.uba.fi.nicodiaz.mascota;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.utils.ParseProxyObject;


public class MascotaDetalleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    //int mutedColor = R.attr.colorPrimary;
    private SliderLayout photo_slider;
    private SliderLayout video_slider;
    private ImageView headerImage;
    private FloatingActionButton FAB;
    private MapView map;
    private GoogleMap mMap;
    private String petName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mascota_detalle);

        ParseProxyObject ppo = (ParseProxyObject) getIntent().getSerializableExtra("Pet");
        ArrayList<String> urlPhotos = getIntent().getStringArrayListExtra("UrlPhotos");
        ArrayList<String> urlVideos = getIntent().getStringArrayListExtra("UrlVideos");
        AdoptionPet adoptionPet = new AdoptionPet(ppo);
        petName = adoptionPet.getName();

        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(petName);

        FAB = (FloatingActionButton) findViewById(R.id.FAB_adoptar);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MascotaDetalleActivity.this, "Próximamente podrás adoptarlo", Toast.LENGTH_SHORT).show();
            }
        });


        // Cargo la Foto en el header
        headerImage = (ImageView) findViewById(R.id.header);

        final ParseImageView imageView = new ParseImageView(this);
        ParseFile photoFile = adoptionPet.getPicture();
        if (photoFile != null) {
            imageView.setParseFile(photoFile);
            imageView.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {

                    try {
                        headerImage.setImageDrawable(imageView.getDrawable());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }


        loadInformacionBasica(adoptionPet);
        loadInformacionSocial(adoptionPet);

        // TODO: si descomentamos esto, se ve el fondo transparente arriba, pero hay que validar:
        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pikachu);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                mutedColor = palette.getMutedColor(R.color.ColorPrimary);
                collapsingToolbar.setContentScrimColor(mutedColor);
                collapsingToolbar.setStatusBarScrimColor(R.color.black_trans80);
            }

        });*/


        photo_slider = (SliderLayout) findViewById(R.id.photo_slider);
        final HashMap<String, String> photos = new HashMap<>();
        int aux = 0;
        for (String url : urlPhotos) {
            photos.put("Photo " + aux, url);
            aux++;
        }

        for (String name : photos.keySet()) {
            DefaultSliderView slide = new DefaultSliderView(this);
            slide.image(photos.get(name));
            slide.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            photo_slider.addSlider(slide);
        }

        photo_slider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        photo_slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));


        video_slider = (SliderLayout) findViewById(R.id.video_slider);

        final HashMap<String, String> videos = new HashMap<>();
        aux = 0;
        for (String urlVideo : urlVideos) {
            videos.put("Video " + aux, urlVideo);
            aux++;
        }

        if (videos.isEmpty()) { // TODO: o otro metodo, la query de base de datos no devolvio datos
            CardView cardview_video = (CardView) findViewById(R.id.cardview_video);
            cardview_video.setVisibility(View.GONE);
        } else {
            for (final String name : videos.keySet()) {
                TextSliderView slide = new TextSliderView(this);
                slide.image("http://img.youtube.com/vi/" + videos.get(name) + "/mqdefault.jpg");
                slide.setScaleType(BaseSliderView.ScaleType.CenterCrop);
                slide.description("Reproducir");
                slide.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView baseSliderView) {
                        Toast.makeText(baseSliderView.getContext(), "Click", Toast.LENGTH_SHORT).show();
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videos.get(name)));
                            intent.putExtra("force_fullscreen", true); // TODO: en versiones viejas de youtube puede no funcionar.
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videos.get(name)));
                            startActivity(intent);
                        }
                    }
                });
                video_slider.addSlider(slide);
            }
            video_slider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
            video_slider.setCustomAnimation(new DescriptionAnimation());
            video_slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator_video));
        }

        setUpMapIfNeeded();
    }


    private void loadInformacionBasica(AdoptionPet adoptionPet) {

        TextView textView = (TextView) findViewById(R.id.infSexoPet);
        textView.setText(adoptionPet.getGender());

        textView = (TextView) findViewById(R.id.infRazaPet);
        if (adoptionPet.getBreed() == null || adoptionPet.getBreed().isEmpty()) {
            textView.setText(R.string.raza_desconocida);
        } else {
            textView.setText(adoptionPet.getBreed());
        }

        textView = (TextView) findViewById(R.id.infEdadPet);
        textView.setText(adoptionPet.getAgeRange());

        textView = (TextView) findViewById(R.id.infDescPet);
        textView.setText(adoptionPet.getDescription());

    }

    private void loadInformacionSocial(AdoptionPet adoptionPet) {
        TextView textView = (TextView) findViewById(R.id.infRelacionNiños);
        textView.setText(adoptionPet.getChildren());

        textView = (TextView) findViewById(R.id.infRelacionAnimales);
        textView.setText(adoptionPet.getOtherPets());

        String socialNotes = adoptionPet.getSocialNotes();
        if (!socialNotes.isEmpty()) {
            textView = (TextView) findViewById(R.id.infComentariosSocial);
            textView.setText(socialNotes);
        } else {
            textView = (TextView) findViewById(R.id.comentariosSocialLabel);
            textView.setVisibility(View.GONE);

            textView = (TextView) findViewById(R.id.infComentariosSocial);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        photo_slider.stopAutoCycle();
        video_slider.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        photo_slider.startAutoCycle();
        video_slider.stopAutoCycle();
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mascota_detalle, menu);
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
        overridePendingTransition(R.anim.slide_in_2, R.anim.slide_out_2);
    }


    private void volverAtras() {
        finish();
        overridePendingTransition(R.anim.slide_in_2, R.anim.slide_out_2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLng fiuba = new LatLng(-34.617811, -58.368700);
        mMap.addMarker(new MarkerOptions().position(fiuba).title("FIUBA")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(fiuba, 16)));
        try {
            TextView ubicacion = (TextView) findViewById(R.id.text_ubicacion);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(fiuba.latitude, fiuba.longitude, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                sb.append(address.getThoroughfare()).append(" ").append(address.getSubThoroughfare()).append(", ").append(address.getLocality());
            }
            ubicacion.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
