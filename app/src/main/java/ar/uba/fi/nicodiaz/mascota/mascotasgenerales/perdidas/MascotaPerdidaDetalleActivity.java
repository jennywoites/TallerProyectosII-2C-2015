package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.perdidas;

import android.content.Intent;
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

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.google.samples.apps.iosched.ui.widget.ViewPagerAdapter;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.NewCommentActivity;
import ar.uba.fi.nicodiaz.mascota.model.CommentService;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

public class MascotaPerdidaDetalleActivity extends AppCompatActivity {
    CharSequence Titles[];
    int NumbOfTabs = 2;
  //  FloatingActionButton FAB_adopt;
    FloatingActionButton FAB_comment;

/*    @Override
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
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota_perdida_detalle);
        Pet pet = PetServiceFactory.getInstance().getSelectedPet();
        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(pet.getName());

        /*FAB_adopt = (FloatingActionButton) findViewById(R.id.FAB_adoptar);

        boolean yaAdopto = false;

        if (yaAdopto) {
            disableAdoptFAB();
        } else {
            enableAdoptFAB();
        }*/

        FAB_comment = (FloatingActionButton) findViewById(R.id.FAB_comentar);
        FAB_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MascotaPerdidaDetalleActivity.this, NewCommentActivity.class);
                startActivity(i);
            }
        });

        loadHeader(pet);

        int commentsCount = CommentService.getInstance().getCount(pet.getID());
        Titles = new CharSequence[]{"Información", "Comentarios (" + String.valueOf(commentsCount) + ")"};

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
          //              FAB_adopt.setVisibility(View.VISIBLE);
                        FAB_comment.setVisibility(View.GONE);
                        break;
                    case 1:
            //            FAB_adopt.setVisibility(View.GONE);
                        FAB_comment.setVisibility(View.VISIBLE);
                        break;
                    default:
              //          FAB_adopt.setVisibility(View.GONE);
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
        getMenuInflater().inflate(R.menu.menu_mascota_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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