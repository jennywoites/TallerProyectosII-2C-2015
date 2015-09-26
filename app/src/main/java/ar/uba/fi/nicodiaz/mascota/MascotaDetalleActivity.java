package ar.uba.fi.nicodiaz.mascota;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import java.util.ArrayList;
import java.util.HashMap;

import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.utils.ParseProxyObject;


public class MascotaDetalleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    //int mutedColor = R.attr.colorPrimary;
    private SliderLayout photo_slider;
    private SliderLayout video_slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mascota_detalle);

        ParseProxyObject ppo = (ParseProxyObject) getIntent().getSerializableExtra("Pet");
        ArrayList<String> urlPhotos = getIntent().getStringArrayListExtra("UrlPhotos");
        AdoptionPet adoptionPet = new AdoptionPet(ppo);
        String petName = adoptionPet.getName();

        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(petName);


        photo_slider = (SliderLayout) findViewById(R.id.photo_slider);

        // Cargo la Foto en el header
        final ImageView header = (ImageView) findViewById(R.id.header);
        final ParseImageView imageView = new ParseImageView(this);
        ParseFile photoFile = adoptionPet.getPicture();
        if (photoFile != null) {
            imageView.setParseFile(photoFile);
            imageView.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    header.setImageDrawable(imageView.getDrawable());
                }
            });
        }

        loadInformacionBasica(adoptionPet);

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


/*        photo_slider = (SliderLayout) findViewById(R.id.photo_slider);

        // TODO: pedir de la base de datos con el ID recibido por intent
        HashMap<String, Integer> photos = new HashMap<>();
        photos.put("Photo 0", getResources().getIdentifier(petName.toLowerCase(), "drawable", getPackageName()));
        photos.put("Photo 1", getResources().getIdentifier((petName + "1").toLowerCase(), "drawable", getPackageName()));
        photos.put("Photo 2", getResources().getIdentifier((petName + "2").toLowerCase(), "drawable", getPackageName()));
        photos.put("Photo 3", getResources().getIdentifier((petName + "3").toLowerCase(), "drawable", getPackageName()));
*/
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


        // TODO: pedir de la base de datos con el ID recibido por el intent el id de video que tenga:
        // El id del video es el que aparece en la url de youtube del mismo. Eso habria que guardar.
        String id0 = "ycdcDFuGarM";
        String id1 = "TSC8p9-eBNc";
        String id2 = "RWSy24AVnZk";
        final HashMap<String, String> videos = new HashMap<>();
        videos.put("Video 0", id0);
        videos.put("Video 1", id1);
        videos.put("Video 2", id2);

        video_slider = (SliderLayout) findViewById(R.id.video_slider);

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
}
