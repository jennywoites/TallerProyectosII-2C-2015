package ar.uba.fi.nicodiaz.mascota;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.nicodiaz.mascota.model.VersionModel;
import ar.uba.fi.nicodiaz.mascota.utils.SimpleRecyclerAdapter;


public class MascotaDetalleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    int mutedColor = R.attr.colorPrimary;
    private RecyclerView recyclerView;
    private SimpleRecyclerAdapter simpleRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mascota_detalle);

        String petName = getIntent().getStringExtra("ID");
        // TODO: pedir cosas a la base de datos con ese id.

        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(petName);

        ImageView header = (ImageView) findViewById(R.id.header);
        int id = getResources().getIdentifier(petName.toLowerCase(), "drawable", getPackageName()); // TODO: Esto debería salir de la base de datos
        header.setBackgroundResource(id);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.pikachu);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                mutedColor = palette.getMutedColor(R.color.ColorPrimary);
                collapsingToolbar.setContentScrimColor(mutedColor);
                collapsingToolbar.setStatusBarScrimColor(R.color.black_trans80);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.scrollableView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<String> listData = new ArrayList<>();
        int ct = 0;
        for (int i = 0; i < VersionModel.data.length * 2; i++) {
            listData.add(VersionModel.data[ct]);
            ct++;
            if (ct == VersionModel.data.length) {
                ct = 0;
            }
        }

        if (simpleRecyclerAdapter == null) {
            simpleRecyclerAdapter = new SimpleRecyclerAdapter(listData);
            recyclerView.setAdapter(simpleRecyclerAdapter);
        }

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
