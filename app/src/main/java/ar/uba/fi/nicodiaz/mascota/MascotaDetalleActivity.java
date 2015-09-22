package ar.uba.fi.nicodiaz.mascota;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.HashMap;


public class MascotaDetalleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    int mutedColor = R.attr.colorPrimary;
    private SliderLayout photo_slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mascota_detalle);

        String petName = getIntent().getStringExtra("ID");
        // TODO: pedir cosas a la base de datos con ese id.

        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(petName);

        ImageView header = (ImageView) findViewById(R.id.header);
        int id = getResources().getIdentifier(petName.toLowerCase(), "drawable", getPackageName()); // TODO: Esto debería salir de la base de datos
        header.setBackgroundResource(id);

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

        // TODO: pedir de la base de datos con el ID recibido por intent
        HashMap<String, Integer> photos = new HashMap<>();
        photos.put("Photo 0", getResources().getIdentifier(petName.toLowerCase(), "drawable", getPackageName()));
        photos.put("Photo 1", getResources().getIdentifier((petName + "1").toLowerCase(), "drawable", getPackageName()));
        photos.put("Photo 2", getResources().getIdentifier((petName + "2").toLowerCase(), "drawable", getPackageName()));
        photos.put("Photo 3", getResources().getIdentifier((petName + "3").toLowerCase(), "drawable", getPackageName()));

        for (String name : photos.keySet()) {
            DefaultSliderView slide = new DefaultSliderView(this);
            slide.image(photos.get(name));
            slide.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            photo_slider.addSlider(slide);
        }

        photo_slider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        photo_slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        photo_slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));


        // TODO: pedir de la base de datos con el ID recibido por el intent el id de video que tenga:
        // El id del video es el que aparece en la url de youtube del mismo. Eso habria que guardar.

        final String video_id = "ycdcDFuGarM";

        Button video_button = (Button) findViewById(R.id.video_button);
        video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Click",Toast.LENGTH_SHORT).show();
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video_id));
                    intent.putExtra("force_fullscreen", true); // TODO: en versiones viejas de youtube puede no funcionar.
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video_id));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        photo_slider.stopAutoCycle();
        super.onStop();
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
