package ar.uba.fi.nicodiaz.mascota.MascotasGenerales.Perdidas;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.google.samples.apps.iosched.ui.widget.ViewPagerAdapter;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import ar.uba.fi.nicodiaz.mascota.MascotasGenerales.NewCommentActivity;
import ar.uba.fi.nicodiaz.mascota.MascotasGenerales.SolicitarAdopcionActivity;
import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;

public class MascotaPerdidaDetalleActivity extends AppCompatActivity {

    CharSequence Titles[] = {"Información", "Comentarios"}; // TODO: obtener la cantidad de comentarios desde la base de datos.
    int NumbOfTabs = 1;
    FloatingActionButton FAB_adopt;
    FloatingActionButton FAB_comment;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Si dio adoptar:
        if (resultCode == Activity.RESULT_OK) {
            disableAdoptFAB();
        }
    }

    private void enableAdoptFAB() {
        FAB_adopt.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ColorPrimary)));
        FAB_adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MascotaPerdidaDetalleActivity.this, SolicitarAdopcionActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }

    private void disableAdoptFAB() {
        FAB_adopt.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Disabled)));
        FAB_adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MascotaPerdidaDetalleActivity.this, "Ya ha enviado una solicitud de adopción.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_mascota_detalle);
        setContentView(R.layout.activity_mascota_perdida_detalle);

        Pet pet = PetService.getInstance().getSelectedPet();

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(pet.getName());

        FAB_adopt = (FloatingActionButton) findViewById(R.id.FAB_adoptar);

        // TODO: consultar a base de datos si este usuario ya habia mandado una solicitud de adopcion (Y esta activa):
        boolean yaAdopto = false;
        if (yaAdopto) {
            disableAdoptFAB();
        } else {
            enableAdoptFAB();
        }

        FAB_comment = (FloatingActionButton) findViewById(R.id.FAB_comentar);
        FAB_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MascotaPerdidaDetalleActivity.this, NewCommentActivity.class);
                startActivity(i);
            }
        });

        loadHeader(pet);

        ViewPagerAdapter adapter = new ViewPagerMascotaPerdidaDetalleAdapter(getSupportFragmentManager(), Titles, NumbOfTabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
                appBarLayout.setExpanded(false, true);

                switch (position) {
                    case 0:
                        FAB_adopt.setVisibility(View.VISIBLE);
                        FAB_comment.setVisibility(View.GONE);
                        break;
                    case 1:
                        FAB_adopt.setVisibility(View.GONE);
                        FAB_comment.setVisibility(View.VISIBLE);
                        break;
                    default:
                        FAB_adopt.setVisibility(View.GONE);
                        FAB_comment.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.ColorPrimary);
            }
        });
        tabs.setViewPager(pager);
    }

    private void loadHeader(Pet pet) {
        final ImageView headerImage = (ImageView) findViewById(R.id.header);

        final ParseImageView imageView = new ParseImageView(this);
        ParseFile photoFile = pet.getPicture();
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